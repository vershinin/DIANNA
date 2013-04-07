package org.dianna.core.serialization.impl;

import java.lang.reflect.Type;

import com.google.bitcoin.core.Sha256Hash;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Sha256Serializer implements JsonSerializer<Sha256Hash>, JsonDeserializer<Sha256Hash> {

	@Override
	public Sha256Hash deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return new Sha256Hash(json.getAsString());
	}

	@Override
	public JsonElement serialize(Sha256Hash src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

}
