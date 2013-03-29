package org.dianna.tests.network.server;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.DiscoverBuilder;

import org.dianna.network.DiannaRawDataReplay;
import org.dianna.network.server.DiannaServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DiannaServerTest {
	@InjectMocks
	private DiannaServer server;

	@Mock
	private Peer peer;

	@Mock
	private PeerMaker peerMaker;
	
	@Mock
	private DiscoverBuilder discoverBuilder;
	
	@Mock
	private DiannaRawDataReplay replay;

	@Before
	public void init() throws IOException {
		given(peerMaker.makeAndListen()).willReturn(peer);
		given(peer.discover()).willReturn(discoverBuilder);
		server.setPeerMaker(peerMaker);
		server.setReplay(replay);
	}

	@Test
	public void shouldStartListening() throws IOException, InterruptedException {
		// given
		server.start();
		// then
		verify(peerMaker.makeAndListen(), atLeastOnce());
	}

}
