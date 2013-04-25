package org.dianna.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.Random;

import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.builder.BroadcastBuilder;
import net.tomp2p.p2p.builder.DiscoverBuilder;
import net.tomp2p.p2p.builder.SendDirectBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

import org.dianna.core.exception.InvalidMessageException;
import org.dianna.core.message.Message;
import org.dianna.core.serialization.MessageSerializer;
import org.dianna.core.settings.DiannaSettings;
import org.dianna.network.internal.DiannaRawDataReplay;
import org.dianna.network.internal.PeerFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import com.beust.jcommander.internal.Maps;

public class DiannaPeer {

	private DiannaSettings settings;

	private Peer peer;
	private PeerFactory peerFactory;

	private DiannaRawDataReplay replay;
	private MessageSerializer serializer;

	public DiannaPeer(DiannaSettings settings, PeerFactory peerFactory) {
		this.peerFactory = peerFactory;
		this.settings = settings;
	}

	/**
	 * Start listening
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void listen() throws IOException, InterruptedException {
		peer = peerFactory.makeAndListen();
		peer.getDirectDataRPC().setReply(replay);
	}

	/**
	 * Bootstrap dianna peer using given internet adress
	 * 
	 * @param adress
	 * @throws InterruptedException
	 */
	public void bootstrap(InetAddress adress) throws InterruptedException {
		DiscoverBuilder discoverBuilder = peer.discover();
		discoverBuilder.setInetAddress(adress);
		FutureDiscover bootstrap = discoverBuilder.start();
		bootstrap.await();
	}

	public Message sendMessage(PeerAddress peerAddress, Message message) throws InterruptedException {
		byte[] data = serializer.serialize(message);

		SendDirectBuilder directBuilder = peer.sendDirect(peerAddress);
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
		try {
			return serializer.deserialize(responseData);
		} catch (InvalidMessageException e) {
			return null;
		}
	}

	public void broadcast(Message message) {
		BroadcastBuilder broadcast = peer.broadcast(new Number160(new Random()));
		Map<Number160, Data> dataMap = Maps.newHashMap();
		byte[] serialized = serializer.serialize(message);
		dataMap.put(new Number160(), new Data(serialized));
		broadcast.setDataMap(dataMap);
		broadcast.start(); //
	}

	public DiannaRawDataReplay getReplay() {
		return replay;
	}

	public void setReplay(DiannaRawDataReplay replay) {
		this.replay = replay;
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public PeerFactory getPeerFactory() {
		return peerFactory;
	}

	public void setPeerFactory(PeerFactory peerFactory) {
		this.peerFactory = peerFactory;
	}

	public void setSerializer(MessageSerializer serializer) {
		this.serializer = serializer;
	}

}
