package org.dianna.core.serialization.impl;

import java.lang.reflect.Type;

import org.bouncycastle.util.encoders.Base64;

import com.google.bitcoin.core.ECKey;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EcKeySerializer implements JsonSerializer<ECKey>, JsonDeserializer<ECKey> {

	@Override
	public ECKey deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		byte[] pubKey = Base64.decode(json.getAsString());
		return new ECKey(null, pubKey);
	}

	@Override
	public JsonElement serialize(ECKey src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(new String(Base64.encode(src.getPubKey())));
	}

}
