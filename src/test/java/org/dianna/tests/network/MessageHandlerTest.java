package org.dianna.tests.network;

import static org.junit.Assert.assertTrue;

import org.dianna.core.message.Handshake;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.network.MessageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageHandlerTest {

	@InjectMocks
	private MessageHandler handler;

	@Test
	public void shouldHandleHandshakeMessage() {
		//when
		Message response = handler.handleMessage(null, new Handshake());
		//then
		assertTrue(response.getType() == MessageType.HANDSHAKE);
	}

}
