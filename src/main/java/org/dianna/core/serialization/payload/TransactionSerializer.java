package org.dianna.core.serialization.payload;

import org.dianna.core.entity.Entity;
import org.dianna.core.message.Message.MessageType;

public class TransactionSerializer implements PayloadSerializer {

	@Override
	public byte[] serialize(Entity message) {
		return null;
	}

	@Override
	public Entity deserialize(byte[] byteArray) {
		return null;
	}

	@Override
	public MessageType getType() {
		return MessageType.TRANSACTION;
	}

}
