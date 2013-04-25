package org.dianna.bitcoinlite;

import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Transaction;

public interface BitcoinClient {

	public Sha256Hash getCoinbase(Sha256Hash blockHash, int index);
	public Transaction getTransaction(Sha256Hash transactionHash);

	public void connectToNetwork();
}
