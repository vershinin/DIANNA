package org.dianna.network.internal;

import java.io.IOException;
import java.util.Random;

import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

import org.dianna.core.settings.DiannaSettings;
import org.dianna.network.handler.broadcast.DiannaBroadcastHandler;
import org.dianna.network.handler.broadcast.DiannaBroadcastRpc;

/**
 * This class creates tomp2p peers
 * 
 * @author ivan
 * 
 */
public class PeerFactory {
	private DiannaSettings settings;
	private DiannaBroadcastHandler broadcastHandler;
	private Random rnd;

	public PeerFactory(DiannaSettings settings, DiannaBroadcastHandler broadcastHandler) {
		this.settings = settings;
		this.broadcastHandler = broadcastHandler;
	}

	private PeerMaker createPeer() {
		Number160 peerId = settings.getPeerId();
		rnd = new Random();
		if (settings.isRandomId()) {
			peerId = new Number160(rnd);
		}

		PeerMaker peerMaker = new PeerMaker(peerId);
		peerMaker.setTcpPort(settings.getTcpPort());
		peerMaker.setUdpPort(settings.getUdpPort());
		peerMaker.setBroadcastHandler(broadcastHandler);
		peerMaker.setRandom(rnd);
		return peerMaker;
	}

	public Peer makeAndListen() throws IOException {
		Peer peer = createPeer().makeAndListen();
		broadcastHandler.setPeer(peer);
		broadcastHandler.setRnd(rnd);
		//peer.setBroadcastRPC(new DiannaBroadcastRpc(peer.getPeerBean(), peer.getConnectionBean(), broadcastHandler));
		return peer;
	};
}
