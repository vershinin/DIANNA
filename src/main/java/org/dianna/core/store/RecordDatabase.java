package org.dianna.core.store;

import java.util.Map;

import org.dianna.core.entity.DomainTransaction;

import com.google.bitcoin.core.ECKey;
import com.google.common.collect.Maps;

/**
 * This class holds data records, and can be updated by adding transactions
 * 
 * @author ivan
 * 
 */
public class RecordDatabase {
	private Map<String, String> records = Maps.newHashMap();
	private Map<String, ECKey> publicKeys = Maps.newHashMap();

	public ECKey getPublicKey(String domain) {
		return publicKeys.get(domain);
	}

	public String getRecord(String domain) {
		return records.get(domain);
	}

	public void processTransaction(DomainTransaction tx) {
		// validate transaction
		// update records
		records.put(tx.getDomain(), tx.getValue());
		publicKeys.put(tx.getDomain(), tx.getNextPubkey());
	}
}
