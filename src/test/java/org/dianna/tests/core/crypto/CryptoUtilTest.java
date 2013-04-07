package org.dianna.tests.core.crypto;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dianna.core.crypto.CryptoUtil;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.tests.builders.TestDataFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;

@RunWith(MockitoJUnitRunner.class)
public class CryptoUtilTest {

	@Test
	public void shouldValidateSignedTransaction() {
		// given
		ECKey key = new ECKey();
		DomainTransaction transaction = TestDataFactory.buildCorrectTransaction();
		transaction.setNextPubkey(key);
		String signature = CryptoUtil.signTransaction(transaction, key);
		transaction.setSignature(signature);

		// when
		boolean isValid = CryptoUtil.verifyTransaction(transaction, key);

		// then
		assertTrue(isValid);
	}

	@Test
	public void shouldVerifyCorrectTransaction() {
		// given
		DomainTransaction transaction = TestDataFactory.buildCorrectTransaction();

		// when
		boolean isValid = CryptoUtil.verifyTransaction(transaction, transaction.getNextPubkey());

		// then
		assertTrue(isValid);
	}

	@Test
	public void shouldFailVerificationIfDomainChanged() {
		// given
		DomainTransaction transaction = TestDataFactory.buildCorrectTransaction();
		transaction.setDomain("new domain");

		// when
		boolean isValid = CryptoUtil.verifyTransaction(transaction, transaction.getNextPubkey());

		// then
		assertFalse(isValid);
	}

	@Test
	public void shouldFailVerificationIfValueChanged() {
		// given
		DomainTransaction transaction = TestDataFactory.buildCorrectTransaction();
		transaction.setValue("new value");

		// when
		boolean isValid = CryptoUtil.verifyTransaction(transaction, transaction.getNextPubkey());

		// then
		assertFalse(isValid);
	}

	@Test
	public void shouldFailVerificationIfFeeTransactionChanged() {
		// given
		DomainTransaction transaction = TestDataFactory.buildCorrectTransaction();
		transaction.setFeeTransaction(Sha256Hash.ZERO_HASH);

		// when
		boolean isValid = CryptoUtil.verifyTransaction(transaction, transaction.getNextPubkey());

		// then
		assertFalse(isValid);
	}

	@Test
	public void shouldFailVerificationIfKeyChanged() {
		// given
		DomainTransaction transaction = TestDataFactory.buildCorrectTransaction();
		transaction.setNextPubkey(new ECKey());

		// when
		boolean isValid = CryptoUtil.verifyTransaction(transaction, transaction.getNextPubkey());

		// then
		assertFalse(isValid);
	}

	@Test
	public void shouldFailVerificationIfPreviousTransactionChanged() {
		// given
		DomainTransaction transaction = TestDataFactory.buildCorrectTransaction();
		transaction.setPrevTransaction(Sha256Hash.ZERO_HASH);

		// when
		boolean isValid = CryptoUtil.verifyTransaction(transaction, transaction.getNextPubkey());

		// then
		assertFalse(isValid);
	}

	@Test
	public void shouldFailVerificationIfSignatureIsEmpty() {
		// given
		DomainTransaction transaction = TestDataFactory.buildCorrectTransaction();
		transaction.setSignature(null);

		// when
		boolean isValid = CryptoUtil.verifyTransaction(transaction, transaction.getNextPubkey());

		// then
		assertFalse(isValid);
	}

	@Test
	public void shouldFailVerificationIfSignatureChanged() {
		// given
		ECKey key = new ECKey();
		DomainTransaction transaction = TestDataFactory.buildCorrectTransaction();
		String signature = CryptoUtil.signTransaction(transaction, key);
		transaction.setSignature(signature);

		// when
		boolean isValid = CryptoUtil.verifyTransaction(transaction, transaction.getNextPubkey());

		// then
		assertFalse(isValid);
	}
}
