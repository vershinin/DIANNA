package org.dianna.core.serialization.impl;

import org.dianna.core.message.BlockMessage;
import org.dianna.core.message.Handshake;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.Ping;
import org.dianna.core.message.Pong;
import org.dianna.core.serialization.MessageSerializer;
import org.joda.time.DateTime;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonMessageSerializer implements MessageSerializer {

	private Gson gson;

	public JsonMessageSerializer() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ECKey.class, new EcKeySerializer());
		builder.registerTypeAdapter(Sha256Hash.class, new Sha256Serializer());
		builder.registerTypeAdapter(DateTime.class, new DateTimeSerialzer());
		gson = builder.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dianna.core.serialization.MessageSerializer#serialize(org.dianna.
	 * core.message.Message)
	 */
	@Override
	public byte[] serialize(Message message) {
		String json = gson.toJson(message);
		return json.getBytes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dianna.core.serialization.MessageSerializer#deserialize(byte[])
	 */
	@Override
	public Message deserialize(byte[] msg) {
		JsonElement element = new JsonParser().parse(new String(msg));
		JsonObject jsonObject = element.getAsJsonObject();
		MessageType messageType = MessageType.valueOf(jsonObject.get("type").getAsString());
		return createMessage(messageType, element);
	}

	private Message createMessage(MessageType messageType, JsonElement element) {
		switch (messageType) {
		case BLOCK:
			return gson.fromJson(element, BlockMessage.class);
		case HANDSHAKE:
			return gson.fromJson(element, Handshake.class);
		case PING:
			return gson.fromJson(element, Ping.class);
		case PONG:
			return gson.fromJson(element, Pong.class);
		case TRANSACTION:
			//
		default:
			break;
		}
		return null;
	}

}
