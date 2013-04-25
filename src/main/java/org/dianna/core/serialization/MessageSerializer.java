package org.dianna.core.serialization;

import org.dianna.core.exception.InvalidMessageException;
import org.dianna.core.message.Message;

public interface MessageSerializer {
	
	public byte[] serialize(Message message);

	public Message deserialize(byte[] message) throws InvalidMessageException;


}