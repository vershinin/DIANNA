package org.dianna.bitcoinlite.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.dianna.core.utils.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.AbstractPeerEventListener;
import com.google.bitcoin.core.Block;
import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.GetDataMessage;
import com.google.bitcoin.core.InventoryItem;
import com.google.bitcoin.core.Message;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Peer;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.StoredBlock;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionInput;
import com.google.bitcoin.core.VerificationException;
import com.google.bitcoin.core.AbstractBlockChain.NewBlockType;
import com.google.bitcoin.discovery.IrcDiscovery;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.MemoryBlockStore;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ListenableFuture;

public class BitcoinClient {
	private static final int MIN_PEERS = 1;

	private static Logger log = LoggerFactory.getLogger(CryptoUtil.class);

	private NetworkParameters params;
	private PeerGroup peerGroup;
	private BlockStore blockStore;
	private BlockChain chain;

	public BitcoinClient(NetworkParameters params) {
		this.params = params;
	}

	public void connect() throws BlockStoreException, InterruptedException, ExecutionException {
		blockStore = new MemoryBlockStore(params);
		chain = new BlockChain(params, blockStore);
		peerGroup = new PeerGroup(params, chain);
		// peerGroup.addPeerDiscovery(new IrcDiscovery("#bitcoinTEST3"));
		peerGroup.start();
		try {
			peerGroup.connectTo(new InetSocketAddress(InetAddress.getLocalHost(), params.port));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ListenableFuture<PeerGroup> future = peerGroup.waitForPeers(MIN_PEERS);
		future.get();
	}

	/**
	 * Get coinbase from bitcoin block
	 * 
	 * @param blockHash
	 * @param coinbaseIdx
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Sha256Hash getCoinbase(Sha256Hash blockHash, int coinbaseIdx) throws BitcoinException {
		Peer peer = Iterables.getFirst(peerGroup.getConnectedPeers(), null);

		if (peer == null) {
			throw new BitcoinException("Not enough peers");
		}

		Block block = null;
		try {
			Future<Block> future = peer.getBlock(blockHash);
			block = future.get();
		} catch (IOException e) {
			throw new BitcoinException(e);
		} catch (InterruptedException e) {
			throw new BitcoinException(e);
		} catch (ExecutionException e) {
			throw new BitcoinException(e);
		}

		if (block == null) {
			return null;
		}

		Transaction coinbaseTransaction = Iterables.getFirst(block.getTransactions(), null);
		if (coinbaseTransaction == null) {
			return null;
		}
		TransactionInput first = Iterables.getFirst(coinbaseTransaction.getInputs(), null);
		if (first == null) {
			return null;
		}

		return Sha256Hash.create(first.getScriptBytes()); // TODO extract using
															// coinbase
	}

	public Future<Transaction> getTransaction(Sha256Hash id) throws BitcoinException {
		Peer peer = null;
		List<Peer> connectedPeers = peerGroup.getConnectedPeers();
		for (Peer p : connectedPeers) {
			if (p.getPeerVersionMessage().clientVersion < 70001) {
				continue;
			}
			peer = p;
		}

		GetDataMessage getdata = new GetDataMessage(params);
		InventoryItem inventoryItem = new InventoryItem(InventoryItem.Type.Transaction, id);
		getdata.addItem(inventoryItem);
		final GetDataFuture<Transaction> future = new GetDataFuture<Transaction>(inventoryItem);

		peer.addEventListener(new AbstractPeerEventListener() {
			@Override
			public void onTransaction(Peer peer, Transaction t) {
				if (future.getItem().hash.equals(t.getHash())) {
					future.setResult(t);
					peer.removeEventListener(this);
				}
			}
		});
		try {
			peer.sendMessage(getdata);
			return future;
		} catch (IOException e) {
			throw new BitcoinException(e);
		}
	}

	private static class GetDataFuture<T extends Message> implements Future<T> {
		private boolean cancelled;
		private final InventoryItem item;
		private final CountDownLatch latch;
		private T result;

		GetDataFuture(InventoryItem item) {
			this.item = item;
			this.latch = new CountDownLatch(1);
		}

		public boolean cancel(boolean b) {
			// Cannot cancel a getdata - once sent, it's sent.
			cancelled = true;
			return false;
		}

		public boolean isCancelled() {
			return cancelled;
		}

		public boolean isDone() {
			return result != null || cancelled;
		}

		public T get() throws InterruptedException, ExecutionException {
			latch.await();
			return Preconditions.checkNotNull(result);
		}

		public T get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
			if (!latch.await(l, timeUnit))
				throw new TimeoutException();
			return Preconditions.checkNotNull(result);
		}

		InventoryItem getItem() {
			return item;
		}

		/** Called by the Peer when the result has arrived. Completes the task. */
		void setResult(T result) {
			// This should be called in the network loop thread for this peer
			this.result = result;
			// Now release the thread that is waiting. We don't need to
			// synchronize here as the latch establishes
			// a memory barrier.
			latch.countDown();
		}
	}
}
