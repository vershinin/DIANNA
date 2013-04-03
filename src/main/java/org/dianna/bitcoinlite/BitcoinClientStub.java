package org.dianna.bitcoinlite;

import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Transaction;

/**
 * This is stub for bitcoin client
 * 
 * @author ivan
 * 
 */
public class BitcoinClientStub implements BitcoinClient {

	@Override
	public Sha256Hash getCoinbase(Sha256Hash blockHash, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction getTransaction(Sha256Hash transactionHash) {
		// TODO Auto-generated method stub
		return null;
	}

}
