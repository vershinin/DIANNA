package org.dianna.tests;

import org.dianna.core.factory.MessageSerializer;
import org.dianna.core.message.Ping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageSerializerTest {
	@InjectMocks
	private MessageSerializer serializer;

	@Test
	public void shouldSerializeCorrectMessage() {
		//when
		serializer.serialize(new Ping(null));
	}
}
