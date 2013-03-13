package org.dianna.tests.bitcoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.bitcoinlite.BitcoinException;
import org.dianna.core.crypto.CryptoUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.MemoryBlockStore;

public class BitcoinClientIT {
	private static Logger log = LoggerFactory.getLogger(BitcoinClientIT.class);

	private static final String TX_ID = "4d5fbd6acdef243d271997b429eeca0da6bf6da7cc21fe3d64caf9b449b67c6a";
	final NetworkParameters params = NetworkParameters.testNet3();

	@Test
	public void downloadBlock() throws BlockStoreException, InterruptedException, ExecutionException, BitcoinException {
		BitcoinClient c = new BitcoinClient(params);
		c.connect();
		Sha256Hash coinbase = c.getCoinbase(new
				Sha256Hash("000000007d811489dbff33cd4bf238a55372087873df8acbe5f1b1e63eb2f6e4"),
				0);
		log.info(coinbase.toString());
		Future<Transaction> future = c.getTransaction(new Sha256Hash(TX_ID));
		Transaction transaction = future.get();
		log.info(transaction.toString());
	}
}
