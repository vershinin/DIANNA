package org.dianna.tests.serialization;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.payload.Block;
import org.dianna.core.message.payload.Payload;
import org.dianna.core.serialization.MessageSerializer;
import org.dianna.core.serialization.impl.ContentSerializer;
import org.dianna.core.serialization.impl.MessageSerializerImpl;
import org.dianna.core.serialization.payload.BlockSerializer;
import org.dianna.tests.factory.BlockFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageSerializerTest {
	@InjectMocks
	private MessageSerializer serializer = new MessageSerializerImpl();

	@Mock
	private ContentSerializer contentSerializer;

	@Test
	public void shouldSerializeAndDeserializeCorrectMessage() {
		// given
		Block block = BlockFactory.createCorrectBlock();
		given(contentSerializer.serialize(eq(MessageType.BLOCK), any(Payload.class))).willReturn(new byte[0]);
		given(contentSerializer.deserialize(eq(MessageType.BLOCK), any(byte[].class))).willReturn(block);

		Message message = new Message(MessageType.BLOCK);
		message.setPayload(block);

		// when
		byte[] messageBytes = serializer.serialize(message);
		Message deserialize = serializer.deserialize(messageBytes);

		// then
		assertEquals(MessageType.BLOCK, deserialize.getType());
		Block payload = (Block) deserialize.getPayload();
		assertNotNull(payload);
	}
}
