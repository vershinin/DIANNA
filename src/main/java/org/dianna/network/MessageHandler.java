package org.dianna.network;

import java.util.Map;

import net.tomp2p.peers.PeerAddress;

import org.dianna.core.message.Handshake;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class MessageHandler {
	final private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	private Map<MessageType, Handler> handlers = Maps.newHashMap();

	private DiannaClient client;

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
		handlers.put(MessageType.PING, new Handler() {

			@Override
			public Message handle(PeerAddress peer, Message message) {
				logger.debug("Recieved ping from {}", peer.getInetAddress());
				return new Pong();
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
		if (handler == null) {
			logger.warn("Cannot handle message. Handler not set.");
			return null;
		}
		return handler.handle(peer, message);
	}

	private interface Handler {
		public Message handle(PeerAddress peer, Message message);
	}

}
