package org.dianna.core.message;

public class Ping extends Message {

	public Ping() {
		super(MessageType.PING);
	}

	@Override
	protected void parse() {
	}

}
