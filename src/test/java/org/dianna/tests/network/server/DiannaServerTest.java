package org.dianna.tests.network.server;

import static org.mockito.BDDMockito.given;

import java.io.IOException;

import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.DiscoverBuilder;

import org.dianna.core.settings.DiannaSettings;
import org.dianna.network.internal.DiannaRawDataReplay;
import org.dianna.network.internal.PeerFactory;
import org.dianna.network.server.DiannaPeer;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DiannaServerTest {

	@Mock
	private Peer peer;

	@Mock
	private PeerMaker peerMaker;

	@Mock
	private DiscoverBuilder discoverBuilder;

	@Mock
	private DiannaRawDataReplay replay;

	@Mock
	private PeerFactory peerFactory;

	private DiannaSettings settings = new DiannaSettings();

	@InjectMocks
	private DiannaPeer server = new DiannaPeer(settings, peerFactory);

	@Before
	public void init() throws IOException {
		given(peerFactory.makeAndListen()).willReturn(peer);
		given(peer.discover()).willReturn(discoverBuilder);
		server.setReplay(replay);
	}

}
