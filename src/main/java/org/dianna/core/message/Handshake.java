package org.dianna.core.message;

public class Handshake extends Message {
	public Handshake() {
		super(MessageType.HANDSHAKE);
	}

	private String userAgent;
	private Integer protocolVersion;

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Integer getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(Integer protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

}
