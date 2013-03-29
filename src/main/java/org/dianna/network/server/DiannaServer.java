package org.dianna.network.server;

import java.io.IOException;

import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;

import org.dianna.network.DiannaRawDataReplay;

public class DiannaServer {

	private Peer peer;
	private PeerMaker peerMaker;

	private DiannaRawDataReplay replay;

	/**
	 * Start listening
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void start() throws IOException, InterruptedException {
		if (getPeerMaker() == null || getReplay() == null) {
			throw new IllegalStateException("Peer maker or replay cannot be null");
		}
		peerMaker.setEnableDirectDataRPC(true);
		peer = peerMaker.makeAndListen();
		
		FutureDiscover bootstrap = peer.discover().setPeerAddress(peer.getPeerAddress()).start();
		bootstrap.await();
		peer.setRawDataReply(getReplay());
	}

	public DiannaRawDataReplay getReplay() {
		return replay;
	}

	public void setReplay(DiannaRawDataReplay replay) {
		this.replay = replay;
	}

	public PeerMaker getPeerMaker() {
		return peerMaker;
	}

	public void setPeerMaker(PeerMaker peerMaker) {
		this.peerMaker = peerMaker;
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

}
