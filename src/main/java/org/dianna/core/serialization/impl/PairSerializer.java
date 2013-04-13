package org.dianna.core.serialization.impl;

import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.bitcoin.core.Sha256Hash;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PairSerializer implements JsonSerializer<Pair>, JsonDeserializer<Pair> {

	@Override
	public Pair deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String string = json.getAsString();
		String[] hashes = StringUtils.split(string, ":");

		return Pair.of(new Sha256Hash(hashes[0]), new Sha256Hash(hashes[1]));
	}

	@Override
	public JsonElement serialize(Pair src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getLeft() + ":" + src.getRight());
	}

}
