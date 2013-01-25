package org.dianna.core.serialization.payload;

import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.payload.Payload;

public interface PayloadSerializer {

	public byte[] serialize(Payload message);

	public Payload deserialize(byte[] byteArray);

	public MessageType getType();
}
