package org.dianna.core.message;

public class ErrorMessage extends Message {

	private String message;

	public ErrorMessage(String message) {
		super(MessageType.ERROR);
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
