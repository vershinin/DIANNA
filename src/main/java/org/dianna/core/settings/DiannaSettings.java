package org.dianna.core.settings;

import java.net.InetAddress;

import net.tomp2p.peers.Number160;

import com.beust.jcommander.Parameter;

public class DiannaSettings {

	@Parameter(names = "-bootstrap", converter = InetAdressConverter.class)
	private InetAddress bootstrapAddress;

	@Parameter(names = "-timeout")
	private Long timeout = 10000L;

	@Parameter(names = "-coinbaseTxIndex")
	private Integer coinbaseTxIndex = 1;

	@Parameter(names = "-namespace")
	private Integer namespace = 1;

	@Parameter(names = "-id", converter = Number160Converter.class)
	private Number160 peerId;

	@Parameter(names = "-randomId")
	private boolean randomId = true;

	@Parameter(names = "-tcpPort")
	private Integer tcpPort = 7700;

	@Parameter(names = "-udpPort")
	private Integer udpPort = 7700;

	@Parameter(names = "-rpcPort")
	private Integer jsonRpcPort = 7701;
	
	@Parameter(names = "-chainId")
	private Integer chainId = 3;

	public InetAddress getBootstrapAddress() {
		return bootstrapAddress;
	}

	public Long getSendMessageTimeout() {
		return timeout;
	}

	public Integer getCoinbaseTxIndex() {
		return coinbaseTxIndex;
	}

	public Integer getNamespace() {
		return namespace;
	}

	public Number160 getPeerId() {
		return peerId;
	}

	public boolean isRandomId() {
		return randomId;
	}

	public Integer getTcpPort() {
		return tcpPort;
	}

	public Integer getUdpPort() {
		return udpPort;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public void setBootstrapAddress(InetAddress bootstrapAddress) {
		this.bootstrapAddress = bootstrapAddress;
	}

	public void setCoinbaseTxIndex(Integer coinbaseTxIndex) {
		this.coinbaseTxIndex = coinbaseTxIndex;
	}

	public void setNamespace(Integer namespace) {
		this.namespace = namespace;
	}

	public void setPeerId(Number160 peerId) {
		this.peerId = peerId;
	}

	public void setRandomId(boolean randomId) {
		this.randomId = randomId;
	}

	public void setTcpPort(Integer tcpPort) {
		this.tcpPort = tcpPort;
	}

	public void setUdpPort(Integer udpPort) {
		this.udpPort = udpPort;
	}

	public Integer getJsonRpcPort() {
		return this.jsonRpcPort ;
	}

	public Integer getChainId() {
		return chainId;
	}

	public void setChainId(Integer chainId) {
		this.chainId = chainId;
	}

}
