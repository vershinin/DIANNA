package org.dianna.tests.serialization;

import static org.junit.Assert.assertEquals;

import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.payload.Payload;
import org.dianna.core.message.payload.Transaction;
import org.dianna.core.serialization.payload.TransactionSerializer;
import org.dianna.tests.factory.TestTransactionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionSerializationTest {

	@InjectMocks
	private TransactionSerializer transactionSerializer;

	@Test
	// Message type should be correct
	public void shouldReturnCorrectMessageType() {
		// when
		MessageType type = transactionSerializer.getType();
		// then
		assertEquals(MessageType.TRANSACTION, type);
	}

	@Test
	public void shouldSerializeAndDeserializeTransaction() {
		// given
		Transaction transaction = TestTransactionFactory.createValidTransaction();

		// when
		byte[] data = transactionSerializer.serialize(transaction);
		Transaction deserializedTrasaction = (Transaction) transactionSerializer.deserialize(data);

		// then
		assertEquals(transaction, deserializedTrasaction);
	}
}
