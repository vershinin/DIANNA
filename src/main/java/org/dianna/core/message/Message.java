package org.dianna.core.message;

public abstract class Message {
	public enum MessageType {
		TRANSACTION,
		BLOCK
	}

	private MessageType type;
	protected byte[] msg;

	public Message(byte[] msg) {
		this.msg = msg;
		parse();
	}

	protected abstract void parse();

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public byte[] serialize() {
		return null;
	}
}
