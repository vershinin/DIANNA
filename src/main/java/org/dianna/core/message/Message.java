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

	private Entity payload;

	public Message(MessageType type) {
		this.type = type;
	}

	public MessageType getType() {
		return type;
	}

	public Entity getPayload() {
		return payload;
	}

	public void setPayload(Entity payload) {
		this.payload = payload;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
