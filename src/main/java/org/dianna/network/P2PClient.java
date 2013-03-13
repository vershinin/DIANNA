package org.dianna.network;

import java.io.IOException;

import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;

import org.dianna.core.message.Message;
import org.dianna.core.serialization.MessageSerializer;
import org.jboss.netty.buffer.ChannelBuffers;

public class P2PClient {
	private PeerMaker peerMaker;
	private Peer peer;
	private MessageReplay replay;
	private MessageSerializer serializer;

	public void connectToNetwork() throws IOException, InterruptedException {
		if (peerMaker == null || replay == null) {
			throw new IllegalStateException("Peer maker or replay cannot be null");
		}
		peer = peerMaker.makeAndListen();
		FutureDiscover bootstrap = peer.discover().start();
		bootstrap.await();
		peer.setRawDataReply(replay);
	}

	public FutureResponse sendMessage(Message message) throws InterruptedException {
		Peer peer = null;
		byte[] buffer = serializer.serialize(message);
		return peer.sendDirect().setBuffer(ChannelBuffers.wrappedBuffer(buffer)).start();
	}

	public void setPeerMaker(PeerMaker peerMaker) {
		this.peerMaker = peerMaker;
	}

	public void setReplay(MessageReplay replay) {
		this.replay = replay;
	}

	public void setSerializer(MessageSerializer serializer) {
		this.serializer = serializer;
	}

}
