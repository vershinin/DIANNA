package org.dianna.network;

import org.dianna.core.message.Message;

public interface MessageListener {

	public Message handleMessage(Message message);

}
