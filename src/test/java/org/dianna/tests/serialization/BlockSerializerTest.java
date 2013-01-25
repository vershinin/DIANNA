package org.dianna.tests.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.dianna.core.exception.ValidationException;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.payload.Block;
import org.dianna.core.serialization.payload.BlockSerializer;
import org.dianna.tests.factory.BlockFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlockSerializerTest {

	@InjectMocks
	private BlockSerializer serializer;

	@Test
	// Should tell, that serializer works only given type
	public void shouldReturnCorrectType() {
		// when
		MessageType type = serializer.getType();

		// then
		assertEquals(type, MessageType.BLOCK);
	}

	@Test
	// Should serialize and deserialize correct block
	public void shouldSerializeAndDeserializeCorrectBlock() throws ValidationException {
		// given
		Block block = BlockFactory.createCorrectBlock();
		// when
		byte[] data = serializer.serialize(block);
		block = (Block) serializer.deserialize(data);
		// then
		assertNotNull(block);

	}
}
