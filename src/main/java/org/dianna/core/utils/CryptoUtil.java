package org.dianna.core.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.dianna.core.entity.DomainTransaction;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;

public class CryptoUtil {
	/**
	 * Sign transaction and return base64 encoded signature
	 * 
	 * @param transaction
	 * @param key
	 * @return
	 */
	public static String signTransaction(DomainTransaction transaction, ECKey key) {
		transaction.setSignature(null);
		Sha256Hash digest = createTransactionDigest(transaction);
		String signature = new String(Base64.encode(key.sign(digest).encodeToDER()));
		return signature;
	}

	/**
	 * We create string from transaction and sign it;
	 * 
	 * @param tx
	 * @return
	 */
	private static Sha256Hash createTransactionDigest(DomainTransaction tx) {
		StringBuilder message = new StringBuilder();
		// NB! do not change order
		message.append(tx.getDomain()).append("\n");
		message.append(tx.getValue()).append("\n");
		message.append(tx.getPrevTransaction()).append("\n");
		message.append(tx.getFeeTransaction()).append("\n");
		message.append(Utils.bytesToHexString(tx.getNextPubkey().getPubKey()));
		return new Sha256Hash(Utils.doubleDigest(message.toString().getBytes()));
	}

	public static boolean verifyTransaction(DomainTransaction transaction, ECKey key) {
		try {
			Sha256Hash message = createTransactionDigest(transaction);
			String signature = transaction.getSignature();
			if (StringUtils.isBlank(signature)) {
				return false;
			}
			byte[] signatureBytes = Base64.decode(signature.getBytes());
			return key.verify(message.getBytes(), signatureBytes);
		} catch (RuntimeException e) {
			// this is required due to key.verify() method wraps IOException
			// into runtime exception
			if (e.getCause() instanceof IOException) {
				return false;
			}
			throw e;
		}
	}
}
