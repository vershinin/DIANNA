package org.dianna.core.crypto;

import org.dianna.core.entity.DomainTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.ECKey.ECDSASignature;
import com.google.bitcoin.core.Sha256Hash;

public class CryptoUtil {
	private static Logger logger = LoggerFactory.getLogger(CryptoUtil.class);

	public void signTransaction(DomainTransaction transaction, ECKey key) {
		transaction.setSignature(null);
		Sha256Hash hash = createSignatureHash(transaction);
		ECDSASignature signature = key.sign(hash);
		transaction.setSignature(signature);
	}

	/**
	 * We create string from transaction and sign it;
	 * 
	 * @param tx
	 * @return
	 */
	private Sha256Hash createSignatureHash(DomainTransaction tx) {
		StringBuilder string = new StringBuilder();
		// IMORTANT! do not change order
		string.append(tx.getDomain());
		string.append(tx.getValue());
		string.append(tx.getPrevTransaction());
		string.append(tx.getFeeTransaction());
		string.append(tx.getNextPubkey());
		return Sha256Hash.create(string.toString().getBytes());
	}

	public boolean verifyTransaction(DomainTransaction transaction, ECKey publicKey) {
		Sha256Hash json = createSignatureHash(transaction);
		return publicKey.verify(json.getBytes(), transaction.getSignature().encodeToDER());
	}
}
