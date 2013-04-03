package org.dianna.core.serialization.payload;

import org.dianna.core.entity.Entity;
import org.dianna.core.message.Message.MessageType;

public interface PayloadSerializer {

	public byte[] serialize(Entity message);

	public Entity deserialize(byte[] byteArray);

	public MessageType getType();
}
