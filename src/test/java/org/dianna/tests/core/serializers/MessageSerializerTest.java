package org.dianna.tests.core.serializers;

import static org.junit.Assert.assertEquals;

import org.dianna.core.crypto.HashUtil;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.message.BlockMessage;
import org.dianna.core.serialization.MessageSerializer;
import org.dianna.core.serialization.impl.JsonMessageSerializer;
import org.dianna.tests.builders.TestDataFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MessageSerializerTest {
	private MessageSerializer serializer = new JsonMessageSerializer();

	@Test
	public void shoudSerializeAndDeserializeMessage() {
		// given
		DiannaBlock block = TestDataFactory.buildCorrectBlock();
		BlockMessage message = new BlockMessage(block);
		byte[] serialized = serializer.serialize(message);

		// when
		BlockMessage deserialized = (BlockMessage) serializer.deserialize(serialized);
		
		// then
		assertEquals(HashUtil.calculateHash(deserialized.getBlock()), HashUtil.calculateHash(block));
	}
	
}
