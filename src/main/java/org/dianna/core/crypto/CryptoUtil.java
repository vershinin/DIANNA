package org.dianna.core.crypto;

import org.dianna.core.message.payload.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.ECKey.ECDSASignature;
import com.google.bitcoin.core.Sha256Hash;

public class CryptoUtil {
	private static Logger logger = LoggerFactory.getLogger(CryptoUtil.class);

	public void signTransaction(Transaction transaction, ECKey key) {
		transaction.setSignature(null);
		String json = transaction.toJson();
		ECDSASignature signature = key.sign(Sha256Hash.create(json.getBytes()));
		transaction.setSignature(signature.encodeToDER());
	}

	public boolean verifyTransaction(Transaction transaction, byte[] publicKey) {
		String json = transaction.toJson();
		return ECKey.verify(json.getBytes(), transaction.getSignature(), publicKey);
	}
}
