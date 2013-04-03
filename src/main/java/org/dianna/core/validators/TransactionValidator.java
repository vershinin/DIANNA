package org.dianna.core.validators;

import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.exception.ValidationException;

import com.google.bitcoin.core.Transaction;

public class TransactionValidator {
	private BitcoinClient bitcoinClient;

	public void validateBlock(DomainTransaction transaction) throws ValidationException {
		// TODO implement transaction validation
		Transaction tx = bitcoinClient.getTransaction(transaction.getFeeTransaction());
		// here we can get transaction, and compare it with our data
	}
}
