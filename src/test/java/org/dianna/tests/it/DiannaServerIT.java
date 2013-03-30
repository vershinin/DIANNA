package org.dianna.tests.it;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Random;

import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.Ping;
import org.dianna.core.serialization.MessageSerializer;
import org.dianna.core.serialization.impl.MessageSerializerImpl;
import org.dianna.network.DiannaClient;
import org.dianna.network.DiannaRawDataReplay;
import org.dianna.network.MessageHandler;
import org.dianna.network.server.DiannaServer;
import org.junit.Before;
import org.junit.Test;

public class DiannaServerIT {
	private DiannaServer server;
	private DiannaClient client;
	private MessageSerializer serializer = new MessageSerializerImpl();

	@Before
	public void init() throws IOException, InterruptedException {
		server = new DiannaServer();
		server.setPeerMaker(new PeerMaker(new Number160(new Random())));
		DiannaRawDataReplay replay = new DiannaRawDataReplay();
		replay.setMessageHandler(new MessageHandler());
		replay.setSerializer(new MessageSerializerImpl());
		server.setReplay(replay);
		server.start();

		client = new DiannaClient();
		client.setPeerMaker(new PeerMaker(new Number160(new Random())).setMasterPeer(server.getPeer()));
		client.setSerializer(new MessageSerializerImpl());
	}

	@Test
	public void testServer() throws IOException, InterruptedException {
		client.connectToNetwork(server.getPeer().getPeerAddress().getInetAddress());
		Ping ping = new Ping();
		Message message = client.sendMessage(server.getPeer().getPeerAddress(), ping);
		assertTrue("message should be pong type", message.getType() == MessageType.PONG);
		client.sendMessage(server.getPeer().getPeerAddress(), message);
	}
	
}
