package org.dianna;

import java.io.IOException;

import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.bitcoinlite.BitcoinClientStub;
import org.dianna.core.serialization.impl.MessageSerializerImpl;
import org.dianna.core.store.BlockStore;
import org.dianna.core.validators.BlockValidator;
import org.dianna.network.handler.BlockHandler;
import org.dianna.network.internal.DiannaRawDataReplay;
import org.dianna.network.internal.MessageHandler;
import org.dianna.network.server.DiannaPeer;

public class Dianna {
	private BitcoinClient bitcoinClient;

	private DiannaPeer peer;
	private DiannaSettings settings;
	private BlockStore blockStore;
	private BlockValidator blockValidator;

	public Dianna() {
		settings = new DiannaSettings();
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

	public void connect() throws IOException, InterruptedException {
		peer.connectToNetwork();
	}

	public String getRecord(String key) {
		return null;
	}

}
