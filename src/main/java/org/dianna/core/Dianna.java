package org.dianna.core;

import java.io.IOException;

import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.bitcoinlite.BitcoinClientStub;
import org.dianna.core.entity.AuxBlock;
import org.dianna.core.entity.AuxData;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.factory.BlockFactory;
import org.dianna.core.message.BlockMessage;
import org.dianna.core.serialization.MessageSerializer;
import org.dianna.core.serialization.impl.JsonMessageSerializer;
import org.dianna.core.settings.DiannaSettings;
import org.dianna.core.store.BlockStore;
import org.dianna.core.utils.CryptoUtil;
import org.dianna.core.validators.BlockValidator;
import org.dianna.network.handler.BlockHandler;
import org.dianna.network.handler.broadcast.DiannaBroadcastHandler;
import org.dianna.network.internal.DiannaRawDataReplay;
import org.dianna.network.internal.MessageHandler;
import org.dianna.network.internal.PeerFactory;
import org.dianna.network.server.DiannaPeer;
import org.dianna.rpc.JsonRpcHandler;
import org.dianna.rpc.JsonRpcServer;
import org.dianna.rpc.handlers.GetAuxBlockHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.thetransactioncompany.jsonrpc2.server.Dispatcher;

/**
 * This is main dianna client class
 * 
 * @author ivan
 * 
 */
public class Dianna {
	private static Logger logger = LoggerFactory.getLogger(Dianna.class);

	private BitcoinClient bitcoinClient;

	private DiannaPeer peer;
	private DiannaSettings settings;
	private BlockStore blockStore;
	private BlockValidator blockValidator;
	private BlockFactory blockFactory;

	private MessageSerializer serializer;

	public Dianna(DiannaSettings settings) {
		this.settings = settings;
		bitcoinClient = new BitcoinClientStub();
		blockValidator = new BlockValidator(bitcoinClient);
		blockStore = new BlockStore(blockValidator);

		initDiannaPeer();

		blockFactory = new BlockFactory(settings);

		peer.setSerializer(serializer);
		initRpc();
	}

	private void initDiannaPeer() {
		DiannaRawDataReplay replay = new DiannaRawDataReplay();
		serializer = new JsonMessageSerializer();
		replay.setSerializer(serializer);
		MessageHandler messageHandler = new MessageHandler();
		messageHandler.addHandler(new BlockHandler(blockStore));
		replay.setMessageHandler(messageHandler);

		DiannaBroadcastHandler broadcastHandler = new DiannaBroadcastHandler(messageHandler, serializer);
		PeerFactory peerFactory = new PeerFactory(settings, broadcastHandler);
		peer = new DiannaPeer(settings, peerFactory);

		peer.setReplay(replay);
	}

	private void initRpc() {
		DomainTransaction t = new DomainTransaction();
		t.setDomain("test");
		t.setValue("etst");
		t.setFeeTransaction(Sha256Hash.ZERO_HASH);
		ECKey key = new ECKey();
		t.setNextPubkey(key);
		t.setSignature(CryptoUtil.signTransaction(t, key));
		addTransaction(t);
		Dispatcher dispatcher = new Dispatcher();
		dispatcher.register(new GetAuxBlockHandler(new DiannaAuxBlockHandler() {
			@Override
			public void postAuxData(Sha256Hash blockHash, AuxData auxData) {
				broadcastBlock(auxData);
			}

			@Override
			public AuxBlock getAuxBlock() {
				AuxBlock aux = new AuxBlock();
				aux.setChainId(settings.getChainId());
				aux.setHash(blockFactory.getCurrentBlockHash().toString());
				aux.setTarget(blockFactory.getCurrentTarget());
				return aux;
			}
		}));
		JsonRpcHandler rpcHandler = new JsonRpcHandler(dispatcher);
		try {
			JsonRpcServer server = new JsonRpcServer(settings, rpcHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHandlers() {

	}

	public void connect() throws IOException, InterruptedException {
		bitcoinClient.connectToNetwork();
		peer.listen();
	}

	public void bootstrap() throws InterruptedException {
		peer.bootstrap(settings.getBootstrapAddress());
		DiannaBlock block = blockFactory.build();
		peer.broadcast(new BlockMessage(block));
	}

	public String getBlockHash() {
		return blockFactory.getCurrentBlockHash().toString();
	}

	public void addTransaction(DomainTransaction tx) {
		blockFactory.addTransaction(tx);
	}

	public void broadcastBlock(AuxData auxData) {
		DiannaBlock block = blockFactory.build();
		//block.setAuxBranch(auxData.getAuxMerkleBranch());
		block.setCoinbaseTxIndex(auxData.getCoinbaseTxIndex());
		block.setParentBlockHash(auxData.getParentBlockHash());

		try {
			blockValidator.validateBlock(block);
		} catch (ValidationException e) {
			logger.error("New block is invalid!");
			return; // TODO
		}
		peer.broadcast(new BlockMessage(block));
	}

	public String getRecord(String key) {
		return null;
	}

}
