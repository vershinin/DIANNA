package org.dianna.core.message;

public abstract class DMessage {
	protected byte[] msg;

	public DMessage(byte[] msg) {
		this.msg = msg;
		parse();
	}

	protected abstract void parse();
}
