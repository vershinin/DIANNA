package org.dianna.network.internal;

import java.util.Map;

import net.tomp2p.peers.PeerAddress;

import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.network.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class MessageHandler {
	final private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	private Map<MessageType, Handler> handlers = Maps.newHashMap();

	public void addHandler(Handler handler) {
		MessageType type = handler.getType();
		if (handlers.containsKey(type)) {
			logger.warn("There are already exist handler for {}", type);
		}
		handlers.put(type, handler);
	}

	public void removeHandler(Handler handler) {
		handlers.remove(handler.getType());
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

}
