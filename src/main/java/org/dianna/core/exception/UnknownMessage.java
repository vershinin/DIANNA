package org.dianna.core.exception;

public class UnknownMessage extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownMessage(String string) {
		super(string);
	}

	public UnknownMessage(Exception e) {
		super(e);
	}

}