package org.dianna.core.message;


public class Ping extends Message {

	public Ping(byte[] msg) {
		super(msg);
	}

	@Override
	protected void parse() {
	}

}
