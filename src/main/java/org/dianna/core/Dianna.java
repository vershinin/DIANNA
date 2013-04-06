package org.dianna.core;

import java.io.IOException;

import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.bitcoinlite.BitcoinClientStub;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.factory.BlockFactory;
import org.dianna.core.message.BlockMessage;
import org.dianna.core.serialization.impl.MessageSerializerImpl;
import org.dianna.core.store.BlockStore;
import org.dianna.core.validators.BlockValidator;
import org.dianna.network.handler.BlockHandler;
import org.dianna.network.internal.DiannaRawDataReplay;
import org.dianna.network.internal.MessageHandler;
import org.dianna.network.server.DiannaPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public Dianna(DiannaSettings settings) {
		this.settings = settings;
		bitcoinClient = new BitcoinClientStub();
		peer = new DiannaPeer(settings);

		blockValidator = new BlockValidator(bitcoinClient);

		blockStore = new BlockStore(blockValidator);

		DiannaRawDataReplay replay = new DiannaRawDataReplay();
		replay.setSerializer(new MessageSerializerImpl());
		MessageHandler messageHandler = new MessageHandler();
		messageHandler.addHandler(new BlockHandler(blockStore));

		replay.setMessageHandler(messageHandler);
		peer.setReplay(replay);
	}

	private void initHandlers() {

	}

	public void connect() throws IOException, InterruptedException {
		peer.connectToNetwork();
	}

	public String getBlockHash() {
		return null;
	}

	public void addTransaction(String jsonTransaction) {
		DomainTransaction tx = new DomainTransaction();
		blockFactory.addTransaction(tx);
	}

	public void broadcastBlock(String auxData) {
		DiannaBlock block = blockFactory.buildNewBlock();
		block.setAuxBranch(null);
		block.setCoinbaseTxIndex(0);
		block.setParentBlockHash(null);
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