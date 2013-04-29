package org.dianna.tests.core.serializers;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.tuple.Pair;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.exception.InvalidMessageException;
import org.dianna.core.message.BlockMessage;
import org.dianna.core.serialization.MessageSerializer;
import org.dianna.core.serialization.impl.JsonMessageSerializer;
import org.dianna.core.utils.HashUtil;
import org.dianna.tests.builders.TestDataFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.Sha256Hash;

@RunWith(MockitoJUnitRunner.class)
public class MessageSerializerTest {
	private MessageSerializer serializer = new JsonMessageSerializer();

	@Test
	public void shoudSerializeAndDeserializeMessage() throws InvalidMessageException {
		// given
		DiannaBlock block = TestDataFactory.buildCorrectBlock();
		block.getAuxBranch().add(Pair.of(Sha256Hash.ZERO_HASH, Sha256Hash.ZERO_HASH));
		BlockMessage message = new BlockMessage(block);
		byte[] serialized = serializer.serialize(message);

		// when
		BlockMessage deserialized = (BlockMessage) serializer.deserialize(serialized);

		// then
		assertEquals(HashUtil.calculateHash(deserialized.getBlock()), HashUtil.calculateHash(block));
	}

}
