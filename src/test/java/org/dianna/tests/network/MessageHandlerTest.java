package org.dianna.tests.network;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import net.tomp2p.peers.PeerAddress;

import org.dianna.core.message.BlockMessage;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.network.handler.BlockHandler;
import org.dianna.network.handler.Handler;
import org.dianna.network.internal.MessageHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageHandlerTest {

	@InjectMocks
	private MessageHandler handler;

	@Mock
	private Handler blockHandler = new BlockHandler(null);

	@Before
	public void setUp() {
		given(blockHandler.getType()).willReturn(MessageType.BLOCK);
	}

	@Test
	public void shouldBeAbleToAddHandlers() {
		// given
		handler.addHandler(blockHandler);
		// when
		handler.handleMessage(null, new BlockMessage());
		// then
		verify(blockHandler).handle(any(PeerAddress.class), any(Message.class));
	}

	@Test
	public void shouldBeAbleToRemoveHandlers() {
		// given
		handler.addHandler(blockHandler);
		handler.removeHandler(blockHandler);
		// when
		handler.handleMessage(null, new BlockMessage());
		// then
		verify(blockHandler, never()).handle(any(PeerAddress.class), any(Message.class));
	}

}
