package org.dianna.core.serialization.impl;

import org.dianna.core.Protos.DiaMessage;
import org.dianna.core.Protos.DiaMessage.Builder;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.payload.Payload;
import org.dianna.core.serialization.MessageSerializer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

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
		Builder builder = DiaMessage.newBuilder();
		builder.setType(DiaMessage.DiaMessageType.valueOf(message.getType().name()));

		byte[] content = serializeContent(message);
		if (content != null) {
			builder.setContent(ByteString.copyFrom(content));
		}
		return builder.build().toByteArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dianna.core.serialization.MessageSerializer#deserialize(byte[])
	 */
	@Override
	public Message deserialize(byte[] msg) {
		DiaMessage message = null;
		try {
			message = DiaMessage.parseFrom(msg);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		if (message == null) {
			return null;
		}
		MessageType messageType = MessageType.valueOf(message.getType().name());
		Message m = new Message(messageType);

		ByteString content = message.getContent();
		if (content != null) {
			Payload payload = contentSerializer.deserialize(messageType, content.toByteArray());
			m.setPayload(payload);
		}

		return m;

	}

	private byte[] serializeContent(Message message) {
		return contentSerializer.serialize(message.getType(), message.getPayload());
	}

}
