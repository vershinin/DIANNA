package org.dianna.core.serialization.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.payload.Payload;
import org.dianna.core.serialization.payload.PayloadSerializer;

import com.google.common.collect.Maps;

public class ContentSerializer {
	private Map<MessageType, PayloadSerializer> serializersMap = Maps.newHashMap();

	public void addContentSerializers(List<PayloadSerializer> contentSerializers) {
		if (CollectionUtils.isEmpty(contentSerializers)) {
			return;
		}

		for (PayloadSerializer serializer : contentSerializers) {
			MessageType type = serializer.getType();
			if (serializersMap.containsKey(type)) {
				throw new IllegalStateException(MessageFormat.format("There already exists serializer for {0}", type));
			}
			serializersMap.put(type, serializer);
		}
	}

	public byte[] serialize(MessageType messageType, Payload payload) {
		PayloadSerializer serializer = serializersMap.get(messageType);
		if (serializer == null) {
			return null;
		}
		return serializer.serialize(payload);
	}

	public Payload deserialize(MessageType messageType, byte[] payloadBytes) {
		PayloadSerializer serializer = serializersMap.get(messageType);
		if (serializer == null) {
			return null;
		}
		return serializer.deserialize(payloadBytes);
	}
}
