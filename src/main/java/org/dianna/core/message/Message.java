package org.dianna.core.message;

import org.dianna.core.entity.Entity;

public class Message {
	public enum MessageType {
		PING,
		PONG,
		HANDSHAKE,
		TRANSACTION,
		BLOCK,
		ERROR
	}

	private Integer version;

	private MessageType type;

	public Message(MessageType type) {
		this.type = type;
	}

	public MessageType getType() {
		return type;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
