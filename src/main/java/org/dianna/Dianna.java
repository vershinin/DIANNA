package org.dianna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dianna.core.message.payload.Block;
import org.dianna.core.message.payload.Transaction;
import org.dianna.network.DiannaClient;

import com.google.common.collect.Maps;

public class Dianna {

	private DiannaClient client;
	private Map<String, String> values = Maps.newHashMap();

	public void connect() throws IOException, InterruptedException {
		client.connectToNetwork(null);
	}

	public String getRecord(String key) {
		String value = values.get(key);
		return value;

	}

}
