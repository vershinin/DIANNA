package org.dianna.core.exception;

public class InvalidTransactionException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTransactionException(String string) {
		super(string);
	}
}
