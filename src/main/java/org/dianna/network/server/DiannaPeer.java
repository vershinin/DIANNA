package org.dianna.network.server;

import java.io.IOException;

import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.DiscoverBuilder;
import net.tomp2p.p2p.builder.SendDirectBuilder;
import net.tomp2p.peers.PeerAddress;

import org.dianna.core.DiannaSettings;
import org.dianna.core.message.BlockMessage;
import org.dianna.core.message.Message;
import org.dianna.core.serialization.MessageSerializer;
import org.dianna.network.internal.DiannaRawDataReplay;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class DiannaPeer {

	private DiannaSettings settings;

	private Peer peer;
	private PeerMaker peerMaker;

	private DiannaRawDataReplay replay;
	private MessageSerializer serializer;

	public DiannaPeer(DiannaSettings settings) {
		this.settings = settings;
	}

	/**
	 * Start listening
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void connectToNetwork() throws IOException, InterruptedException {
		if (peerMaker == null) {
			throw new IllegalStateException("Peer maker or replay cannot be null");
		}

		peer = peerMaker.makeAndListen();
		DiscoverBuilder discoverBuilder = peer.discover();
		discoverBuilder.setInetAddress(settings.getBootstrapAddress());
		FutureDiscover bootstrap = discoverBuilder.start();
		bootstrap.await();
	}

	public Message sendMessage(PeerAddress peerAddress, Message message) throws InterruptedException {
		byte[] data = serializer.serialize(message);

		SendDirectBuilder directBuilder = peer.sendDirect();
		directBuilder.setPeerAddress(peerAddress);
		directBuilder.setBuffer(ChannelBuffers.wrappedBuffer(data));
		FutureResponse response = directBuilder.start();
		response.await(settings.getSendMessageTimeout());

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

	public void broadcast(BlockMessage blockMessage) {
		
	}

	public DiannaRawDataReplay getReplay() {
		return replay;
	}

	public void setReplay(DiannaRawDataReplay replay) {
		this.replay = replay;
	}

	public PeerMaker getPeerMaker() {
		return peerMaker;
	}

	public void setPeerMaker(PeerMaker peerMaker) {
		this.peerMaker = peerMaker;
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

}
