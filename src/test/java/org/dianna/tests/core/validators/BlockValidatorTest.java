package org.dianna.tests.core.validators;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;

import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.exception.InvalidTransactionException;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.validators.BlockValidator;
import org.dianna.core.validators.TransactionValidator;
import org.dianna.tests.builders.TestDataFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.Sha256Hash;

@RunWith(MockitoJUnitRunner.class)
public class BlockValidatorTest {
	@Mock
	private TransactionValidator transactionValidator;

	@Mock
	private BitcoinClient bitcoinClient;

	@InjectMocks
	private BlockValidator validator = new BlockValidator(bitcoinClient);

	@Test
	public void shouldValidateCorrectBlock() throws ValidationException, InvalidTransactionException {
		// given
		doNothing().when(transactionValidator).validateTransaction(any(DomainTransaction.class));
		given(bitcoinClient.getCoinbase(any(Sha256Hash.class), anyInt())).willReturn(
				new Sha256Hash("fc0c6cf48aa61d9c6df510a78bbcc3a84b29f13530cb3bf329f5c6db52cd6295"));

		DiannaBlock block = TestDataFactory.buildCorrectBlock();
		// when
		validator.validateBlock(block);
		// then

	}
}
