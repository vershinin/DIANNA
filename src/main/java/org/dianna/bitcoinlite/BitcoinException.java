package org.dianna.bitcoinlite;


public class BitcoinException extends Exception {
	private static final long serialVersionUID = 1L;

	public BitcoinException(Exception e) {
		super(e);
	}

	public BitcoinException(String message) {
		super(message);
	}


}
