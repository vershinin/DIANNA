package org.dianna.core.serialization;

import java.util.List;

import org.dianna.core.message.Message;
import org.dianna.core.serialization.payload.PayloadSerializer;

public interface MessageSerializer {

	
	public byte[] serialize(Message message);

	public Message deserialize(byte[] message);


}