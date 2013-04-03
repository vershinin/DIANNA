package org.dianna.tests.network;

import static org.junit.Assert.assertTrue;

import org.dianna.core.message.Handshake;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.network.internal.DiannaRawDataReplay;
import org.dianna.network.internal.MessageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageReplayTest {

	@InjectMocks
	private DiannaRawDataReplay messageReplay;

	@Test
	public void shouldHandleHandshakeMessage() {
	}

}
