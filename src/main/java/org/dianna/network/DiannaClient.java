package org.dianna.network;

import java.io.IOException;
import java.net.InetAddress;

import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.DiscoverBuilder;
import net.tomp2p.p2p.builder.SendDirectBuilder;
import net.tomp2p.peers.PeerAddress;

import org.dianna.core.message.Message;
import org.dianna.core.serialization.MessageSerializer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class DiannaClient {
	private static final int TIMEOUT = 1000;
	private PeerMaker peerMaker;
	private Peer peer;
	private MessageSerializer serializer;

	public void connectToNetwork(InetAddress bootstrapAddress) throws IOException, InterruptedException {
		if (peerMaker == null) {
			throw new IllegalStateException("Peer maker or replay cannot be null");
		}
		
		peer = peerMaker.makeAndListen();
		DiscoverBuilder discoverBuilder = peer.discover();
		discoverBuilder.setInetAddress(bootstrapAddress);
		FutureDiscover bootstrap = discoverBuilder.start();
		bootstrap.await();
	}

	public Message sendMessage(PeerAddress peerAddress, Message message) throws InterruptedException {
		byte[] data = serializer.serialize(message);

		SendDirectBuilder directBuilder = peer.sendDirect();
		directBuilder.setPeerAddress(peerAddress);
		directBuilder.setBuffer(ChannelBuffers.wrappedBuffer(data));
		FutureResponse response = directBuilder.start();
		response.await(TIMEOUT);

		ChannelBuffer buffer = response.getBuffer();
		if (buffer == null) {
			return null;
		}

		byte[] responseData = buffer.array();
		if (responseData == null) {
			return null;
		}
		return serializer.deserialize(responseData);
	}

	public void setPeerMaker(PeerMaker peerMaker) {
		this.peerMaker = peerMaker;
	}

	public void setSerializer(MessageSerializer serializer) {
		this.serializer = serializer;
	}

}
