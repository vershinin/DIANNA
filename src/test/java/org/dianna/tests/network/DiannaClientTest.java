package org.dianna.tests.network;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.InetAddress;

import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.DiscoverBuilder;
import net.tomp2p.p2p.builder.SendDirectBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

import org.dianna.core.message.Handshake;
import org.dianna.network.DiannaClient;
import org.dianna.tests.common.ReturnsSelf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DiannaClientTest {

	@InjectMocks
	private DiannaClient client;

	@Mock
	private PeerMaker peerMaker;

	@Mock
	private Peer peer;

	private SendDirectBuilder sendDirectBuilder = Mockito.mock(SendDirectBuilder.class, new ReturnsSelf());
	private DiscoverBuilder discoverBuilder = Mockito.mock(DiscoverBuilder.class, new ReturnsSelf());

	@Mock
	private FutureResponse futureResponse;

	@Mock
	private FutureDiscover futureDiscover;

	private PeerAddress peerAddress = new PeerAddress(new Number160());
	private InetAddress bootstrapAddress;

	@Before
	public void init() throws IOException {
		bootstrapAddress = InetAddress.getLocalHost();
		client.setPeerMaker(peerMaker);

		given(peerMaker.makeAndListen()).willReturn(peer);
		given(peer.sendDirect()).willReturn(sendDirectBuilder);
		given(sendDirectBuilder.start()).willReturn(futureResponse);

		given(peer.discover()).willReturn(discoverBuilder);
		given(discoverBuilder.start()).willReturn(futureDiscover);

	}

	@Test
	public void shouldConnectToNetwork() throws IOException, InterruptedException {
		// when
		client.connectToNetwork(bootstrapAddress);
		
		// then
		verify(peerMaker.makeAndListen());
	}

	@Test
	public void shouldSendMessage() throws InterruptedException, IOException {
		client.connectToNetwork(bootstrapAddress);
		client.sendMessage(peerAddress, new Handshake());
	}
}
