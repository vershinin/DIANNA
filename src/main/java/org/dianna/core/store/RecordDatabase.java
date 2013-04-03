package org.dianna.core.store;

import java.util.Map;

import org.dianna.core.entity.DomainTransaction;

import com.google.common.collect.Maps;

public class RecordDatabase {
	private Map<String, String> records = Maps.newHashMap();

	public String getRecord(String key) {
		return records.get(key);
	}

	public void processTransaction(DomainTransaction tx) {
		// validate transaction
		// update records
	}
}
