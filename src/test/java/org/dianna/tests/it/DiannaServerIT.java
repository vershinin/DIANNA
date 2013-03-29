package org.dianna.tests.it;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Random;

import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.Ping;
import org.dianna.core.serialization.MessageSerializer;
import org.dianna.core.serialization.impl.MessageSerializerImpl;
import org.dianna.network.DiannaRawDataReplay;
import org.dianna.network.MessageHandler;
import org.dianna.network.server.DiannaServer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Before;
import org.junit.Test;

public class DiannaServerIT {
	private DiannaServer server;
	private MessageSerializer serializer = new MessageSerializerImpl();

	@Before
	public void init() {
		server = new DiannaServer();
		server.setPeerMaker(new PeerMaker(new Number160(new Random())));
		DiannaRawDataReplay replay = new DiannaRawDataReplay();
		replay.setMessageHandler(new MessageHandler());
		MessageSerializerImpl messageSerializer = new MessageSerializerImpl();
		replay.setSerializer(messageSerializer);
		server.setReplay(replay);
	}

	@Test
	public void testServer() throws IOException, InterruptedException {
		server.start();
		PeerMaker pm = new PeerMaker(new Number160(new Random()));
		Peer another = pm.setPorts(7777).makeAndListen();
		Ping ping = new Ping();
		FutureResponse response = another.sendDirect().setPeerAddress(server.getPeer().getPeerAddress())
				.setBuffer(ChannelBuffers.wrappedBuffer(serializer.serialize(ping))).start();
		response.await();
		ChannelBuffer buffer = response.getBuffer();
		Message message = serializer.deserialize(buffer.array());
		assertTrue("message should be pong type", message.getType() == MessageType.PONG);
	}
}
