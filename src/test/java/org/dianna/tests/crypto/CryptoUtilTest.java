package org.dianna.tests.crypto;

import static org.junit.Assert.*;

import org.dianna.core.crypto.CryptoUtil;
import org.dianna.core.message.payload.Transaction;
import org.dianna.tests.factory.TransactionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.ECKey;

@RunWith(MockitoJUnitRunner.class)
public class CryptoUtilTest {
	@InjectMocks
	private CryptoUtil cryptoUtil;

	private ECKey key = new ECKey();

	@Test
	// should sign and verify tranaction signature
	public void shouldSignAndVerifyTransaction() {
		// given
		Transaction t = TransactionFactory.createValidTransaction();

		// when
		byte[] signature = cryptoUtil.signTransaction(t, key);
		t.setSignature(signature);
		
		assertTrue(cryptoUtil.verifyTransaction(t, key.getPubKey()));
	}
}
