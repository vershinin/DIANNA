package org.dianna.core.serialization.payload;

import org.dianna.core.entity.Entity;
import org.dianna.core.message.Message.MessageType;

public class BlockSerializer implements PayloadSerializer {

	private TransactionSerializer transactionSerializer;

	@Override
	public MessageType getType() {
		return MessageType.BLOCK;
	}

	@Override
	public byte[] serialize(Entity entity) {
		return null;
	}

	@Override
	public Entity deserialize(byte[] byteArray) {
		return null;
	}
}
