package org.dianna.core.exception;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ValidationException(String string) {
		super(string);
	}

	public ValidationException(Exception e) {
		super(e);
	}

}
