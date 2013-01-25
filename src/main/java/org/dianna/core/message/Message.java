package org.dianna.core.message;

import org.dianna.core.message.payload.Payload;

public class Message {
	public enum MessageType {
		PING,
		PONG,
		TRANSACTION,
		BLOCK
	}

	private MessageType type;

	private Payload payload;

	protected byte[] msg;

	public Message(byte[] msg) {
		this.msg = msg;
		parse();
	}

	public Message(MessageType type) {
		this.type = type;
	}

	protected void parse(){
		
	};

	public MessageType getType() {
		return type;
	}

	public byte[] serialize() {
		return null;
	}

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}
}
