package org.dianna.network;

import java.util.Map;

import net.tomp2p.peers.PeerAddress;

import org.dianna.core.message.Handshake;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;

import com.google.common.collect.Maps;

public class MessageHandler {

	private Map<MessageType, Handler> handlers = Maps.newHashMap();

	private P2PClient client;

	public MessageHandler() {
		initHandlers();
	}

	private void initHandlers() {
		handlers.put(MessageType.HANDSHAKE, new Handler() {
			@Override
			public Message handle(PeerAddress peer, Message message) {
				Handshake handshake = (Handshake) message;
				return new Handshake();
			}
		});
	}

	/**
	 * Handle message and return response
	 * 
	 * @param message
	 * @return response message
	 */
	public Message handleMessage(PeerAddress peer, Message message) {
		Handler handler = handlers.get(message.getType());
		return handler.handle(peer, message);
	}

	private interface Handler {
		public Message handle(PeerAddress peer, Message message);
	}

}
