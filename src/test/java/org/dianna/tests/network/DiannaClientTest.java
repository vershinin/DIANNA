package org.dianna.tests.network;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;

import org.dianna.core.message.Handshake;
import org.dianna.network.DiannaClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DiannaClientTest {

	@InjectMocks
	private DiannaClient client;
	
	@Mock
	private PeerMaker peerMaker;
	
	@Mock
	private Peer peer;

	@Before
	public void init() throws IOException {
		given(peerMaker.makeAndListen()).willReturn(peer);
		client.setPeerMaker(peerMaker);
	}

	@Test
	public void shouldConnectToNetwork() throws IOException, InterruptedException {
		//when
		client.connectToNetwork();
		//then
		verify(peerMaker.makeAndListen());
	}

	@Test
	public void shouldSendMessage() throws InterruptedException, IOException {
		client.connectToNetwork();
		DiannaClient client2 = new DiannaClient();
		client2.connectToNetwork();
		client.sendMessage(new Handshake());
	}
}