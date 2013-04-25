package org.dianna.network.handler.broadcast;

import java.util.Map;

import net.tomp2p.connection.ChannelCreator;
import net.tomp2p.connection.ConnectionBean;
import net.tomp2p.connection.PeerBean;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.message.Message;
import net.tomp2p.message.Message.Command;
import net.tomp2p.message.Message.Type;
import net.tomp2p.p2p.BroadcastHandler;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.BroadcastRPC;
import net.tomp2p.rpc.RequestHandlerTCP;
import net.tomp2p.rpc.RequestHandlerUDP;
import net.tomp2p.storage.Data;

public class DiannaBroadcastRpc extends BroadcastRPC {
	final private DiannaBroadcastHandler broadcastHandler;

	public DiannaBroadcastRpc(PeerBean peerBean, ConnectionBean connectionBean, DiannaBroadcastHandler broadcastHandler) {
		super(peerBean, connectionBean, null);
		registerIoHandler(Command.BROADCAST_DATA);
		this.broadcastHandler = broadcastHandler;
	}

	public FutureResponse send(final PeerAddress remotePeer, final Number160 messageKey,
			final Map<Number160, Data> dataMap, final ChannelCreator channelCreator, int idleTCPMillis, boolean forceUDP) {
		return send(remotePeer, messageKey, dataMap, 0, channelCreator, idleTCPMillis, forceUDP);
	}

	public FutureResponse send(final PeerAddress remotePeer, final Number160 messageKey,
			final Map<Number160, Data> dataMap, int hopCounter, final ChannelCreator channelCreator, int idleTCPMillis,
			boolean forceUDP) {
		final Message message = createMessage(remotePeer, Command.BROADCAST_DATA, Type.REQUEST_FF_1);
		message.setDataMap(dataMap);
		message.setInteger(hopCounter);
		message.setKey(messageKey);
		final FutureResponse futureResponse = new FutureResponse(message);
		if (!forceUDP) {
			final RequestHandlerTCP<FutureResponse> requestHandler = new RequestHandlerTCP<FutureResponse>(
					futureResponse, getPeerBean(), getConnectionBean(), message);
			return requestHandler.fireAndForgetTCP(channelCreator, idleTCPMillis);
		} else {
			final RequestHandlerUDP<FutureResponse> requestHandler = new RequestHandlerUDP<FutureResponse>(
					futureResponse, getPeerBean(), getConnectionBean(), message);
			return requestHandler.fireAndForgetUDP(channelCreator);
		}
	}

	@Override
	public Message handleResponse(Message message, boolean sign) throws Exception {
		if (!(message.getType() == Type.REQUEST_FF_1 && message.getCommand() == Command.BROADCAST_DATA)) {
			throw new IllegalArgumentException("Message content is wrong");
		}
		
		broadcastHandler.receive(message);
		return message;
	}

	public BroadcastHandler getBroadcastHandler() {
		return broadcastHandler;
	}

}
