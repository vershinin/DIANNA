package org.dianna.network.handler.broadcast;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.BroadcastHandler;
import net.tomp2p.p2p.DefaultBroadcastHandler;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.builder.SendDirectBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import net.tomp2p.utils.ConcurrentCacheMap;
import net.tomp2p.utils.Utils;

import org.dianna.core.exception.InvalidMessageException;
import org.dianna.core.message.ErrorMessage;
import org.dianna.core.message.Message;
import org.dianna.core.serialization.MessageSerializer;
import org.dianna.network.internal.MessageHandler;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiannaBroadcastHandler implements BroadcastHandler {

	final private static Logger logger = LoggerFactory.getLogger(DefaultBroadcastHandler.class);

	private Peer peer;

	private Random rnd;

	final private static int NR = 10;

	final private static int MAX_HOP_COUNT = 7;

	final private ConcurrentCacheMap<Number160, Boolean> cache = new ConcurrentCacheMap<Number160, Boolean>();

	private MessageHandler messageHandler;

	private MessageSerializer serializer;

	public DiannaBroadcastHandler(MessageHandler messageHandler, MessageSerializer serializer) {
		this.messageHandler = messageHandler;
		this.serializer = serializer;
	}

	public void receive(Number160 messageKey, Map<Number160, Data> dataMap, int hopCounter, boolean isUDP) {

		net.tomp2p.message.Message message = new net.tomp2p.message.Message();
		message.setKey(messageKey);
		message.setDataMap(dataMap);
		message.setInteger(hopCounter);
		if (isUDP) {
			message.setUDP();
		}
		receive(message);

	}

	@Override
	public void receive(final net.tomp2p.message.Message message) {
		Number160 messageKey = message.getKey();
		Map<Number160, Data> dataMap = message.getDataMap();
		int hopCount = message.getInteger();
		if (alreadySeen(messageKey)) {
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("got broadcast map " + dataMap);
		}
		if (message.getSender() != null) {
			try {
				for (Data data : dataMap.values()) {
					byte[] data2 = data.getData();
					Message deserialized = serializer.deserialize(data2);
					messageHandler.handleMessage(message.getSender(), deserialized);
				}
			} catch (InvalidMessageException e) {
				byte[] data = serializer.serialize(new ErrorMessage(e.getMessage()));
				SendDirectBuilder sendDirect = peer.sendDirect(message.getSender());
				sendDirect.setBuffer(ChannelBuffers.wrappedBuffer(data));
				FutureResponse response = sendDirect.start();
				return;
			}
		}

		redirect(messageKey, dataMap, hopCount, message.isUDP());
	}

	protected void redirect(final Number160 messageKey, final Map<Number160, Data> dataMap, final int hopCounter,
			final boolean isUDP) {
		if (hopCounter < MAX_HOP_COUNT) {
			if (hopCounter == 0) {
				firstPeer(messageKey, dataMap, hopCounter, isUDP);
			} else {
				otherPeer(messageKey, dataMap, hopCounter, isUDP);
			}
		}
	}

	private boolean alreadySeen(final Number160 messageKey) {
		Boolean isInCache = cache.putIfAbsent(messageKey, Boolean.TRUE);
		if (isInCache != null) {
			if (isInCache == true) {
				cache.put(messageKey, false);
			} else {
				return true;
			}
		}
		return false;
	}

	private void firstPeer(final Number160 messageKey, final Map<Number160, Data> dataMap, final int hopCounter,
			final boolean isUDP) {
		final List<PeerAddress> list = peer.getPeerBean().getPeerMap().getAll();
		for (final PeerAddress peerAddress : list) {
			peer.getConnectionBean().getConnectionReservation().reserve(1)
					.addListener(new BaseFutureAdapter<FutureChannelCreator>() {
						@Override
						public void operationComplete(FutureChannelCreator future) throws Exception {
							FutureResponse futureResponse = peer.getBroadcastRPC().send(peerAddress, messageKey,
									dataMap, hopCounter + 1, future.getChannelCreator(),
									peer.getConnectionBean().getConfiguration().getIdleTCPMillis(), isUDP);
							if (logger.isDebugEnabled()) {
								logger.debug("broadcast to " + peerAddress);
							}
							Utils.addReleaseListener(futureResponse, peer.getConnectionBean()
									.getConnectionReservation(), future.getChannelCreator(), 1);
						}
					});
		}
	}

	private void otherPeer(final Number160 messageKey, final Map<Number160, Data> dataMap, final int hopCounter,
			final boolean isUDP) {
		final List<PeerAddress> list = peer.getPeerBean().getPeerMap().getAll();
		final int max = Math.min(NR, list.size());
		peer.getConnectionBean().getConnectionReservation().reserve(max)
				.addListener(new BaseFutureAdapter<FutureChannelCreator>() {
					@Override
					public void operationComplete(FutureChannelCreator future) throws Exception {
						for (int i = 0; i < max; i++) {
							PeerAddress randomAddress = list.remove(getRnd().nextInt(list.size()));
							FutureResponse futureResponse = peer.getBroadcastRPC().send(randomAddress, messageKey,
									dataMap, hopCounter + 1, future.getChannelCreator(),
									peer.getConnectionBean().getConfiguration().getIdleTCPMillis(), isUDP);
							if (logger.isDebugEnabled()) {
								logger.debug("broadcast to " + randomAddress);
							}
							Utils.addReleaseListener(futureResponse, peer.getConnectionBean()
									.getConnectionReservation(), future.getChannelCreator(), 1);
						}
					}
				});
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public Random getRnd() {
		return rnd;
	}

	public void setRnd(Random rnd) {
		this.rnd = rnd;
	}

}
