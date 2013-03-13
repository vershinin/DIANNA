package org.dianna.tests.factory;

import org.bouncycastle.util.encoders.Hex;
import org.dianna.core.crypto.CryptoUtil;
import org.dianna.core.message.payload.Transaction;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;

public class TestTransactionFactory {

	
	private static final byte[] VALUE = "Domain value".getBytes();
	private static final String FEE_TRANSACTION = "12312321213123";
	private static final int VERSION = 1;
	private static final byte[] DOMAIN = "google.com".getBytes();
	private static byte[] PRIVATE_KEY = Hex.decode("155c572331fe3de390ff0bf82f1dc340d4fd621fe15e0fa4b8b84b046bc3608f");
	private static byte[] PUB_KEY = Hex
			.decode("04b59a5e7b00699d1e0a546364d54fe3863d32378775c28335ba2c9a8cbdbe2e90f1ffa61564254930e6d2f5be5a0e666b40ee09323d61df429002dc19d91309c5");

	public static Transaction createValidTransaction() {
		Transaction transaction = new Transaction();
		transaction.setVersion(VERSION);
		transaction.setDomain(DOMAIN);
		transaction.setFeeTransaction(Sha256Hash.create(FEE_TRANSACTION.getBytes()));
		transaction.setNextPubkey(PUB_KEY);
		transaction.setValue(VALUE);
		transaction.Sign(new ECKey(PRIVATE_KEY, PUB_KEY));// FIXME
		return transaction;
	}
}
