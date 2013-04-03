package org.dianna.core.message;

import org.dianna.core.entity.Entity;

public class Message {
	public enum MessageType {
		PING,
		PONG,
		HANDSHAKE,
		TRANSACTION,
		BLOCK
	}

	private Integer version;
	
	private MessageType type;

	private Entity payload;

	protected byte[] msg;

	public Message(byte[] msg) {
		this.msg = msg;
		parse();
	}

	public Message(MessageType type) {
		this.type = type;
	}

	protected void parse() {

	};

	public MessageType getType() {
		return type;
	}

	public byte[] serialize() {
		return null;
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
