package org.dianna.core.crypto;

import java.text.MessageFormat;

import org.dianna.core.message.payload.Transaction;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;

public class CryptoUtil {

	// NB! Order is important. Think twice before change it
	private static final String SIGNATURE_PATTERN = "version:{0};fee:{1};domain:{2};value:{3};nextPubkey:{4};prevTrans:{5}";

	private String createPatternForSignature(Transaction transaction) {
		Integer version = transaction.getVersion();
		String fee = Utils.bytesToHexString(transaction.getFeeTransaction().getBytes());

		String prevTrans = null;
		if (transaction.getPrevTransaction() != null) {
			prevTrans = Utils.bytesToHexString(transaction.getPrevTransaction().getBytes());
		}

		String domain = Utils.bytesToHexString(transaction.getDomain());
		String value = Utils.bytesToHexString(transaction.getValue());
		String nextPubKey = Utils.bytesToHexString(transaction.getNextPubkey());

		// NB! Order is important. Think twice before change it
		return MessageFormat.format(SIGNATURE_PATTERN, version, fee, domain, value, nextPubKey, prevTrans);
	}

	public byte[] signTransaction(Transaction transaction, ECKey key) {
		return key.sign(createPatternForSignature(transaction).getBytes());
	}

	public boolean verifyTransaction(Transaction transaction, byte[] publicKey) {
		String trans = createPatternForSignature(transaction);
		return ECKey.verify(trans.getBytes(), transaction.getSignature(), publicKey);
	}

}
