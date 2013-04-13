package org.dianna.core.validators;

import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.core.crypto.CryptoUtil;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.exception.InvalidTransactionException;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.store.RecordDatabase;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Transaction;

public class TransactionValidator {
	private BitcoinClient bitcoinClient;
	private RecordDatabase recordDatabase;
	private CryptoUtil cryptoUtil;

	public void validateTransaction(DomainTransaction transaction) throws InvalidTransactionException {
		// TODO implement transaction validation
		Transaction tx = bitcoinClient.getTransaction(transaction.getFeeTransaction());
		// here we can get transaction, and compare it with our data
		ECKey publicKey = recordDatabase.getPublicKey(transaction.getDomain());
		if (!cryptoUtil.verifyTransaction(transaction, publicKey)) {
			throw new InvalidTransactionException("Invalid transaction signature");
		}
	}
}
