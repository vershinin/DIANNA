package org.dianna.core.serialization.impl;

import org.dianna.core.message.BlockMessage;
import org.dianna.core.message.Handshake;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.Ping;
import org.dianna.core.message.Pong;
import org.dianna.core.serialization.MessageSerializer;

public class MessageSerializerImpl implements MessageSerializer {

	private ContentSerializer contentSerializer = new ContentSerializer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dianna.core.serialization.MessageSerializer#serialize(org.dianna.
	 * core.message.Message)
	 */
	@Override
	public byte[] serialize(Message message) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dianna.core.serialization.MessageSerializer#deserialize(byte[])
	 */
	@Override
	public Message deserialize(byte[] msg) {
		return null;

	}

	private Message createMessage(MessageType messageType) {
		switch (messageType) {
		case BLOCK:
			return new BlockMessage();
		case HANDSHAKE:
			return new Handshake();
		case PING:
			return new Ping();
		case PONG:
			return new Pong();
		case TRANSACTION:
			//
		default:
			break;
		}
		return null;
	}

	private byte[] serializeContent(Message message) {
		return contentSerializer.serialize(message.getType(), message.getPayload());
	}

}
