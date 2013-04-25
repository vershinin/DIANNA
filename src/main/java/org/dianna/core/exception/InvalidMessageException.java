package org.dianna.core.exception;

public class InvalidMessageException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidMessageException(String string) {
		super(string);
	}

	public InvalidMessageException(Exception e) {
		super(e);
	}
}
