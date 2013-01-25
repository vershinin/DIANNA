package org.dianna.core.factory;

import org.dianna.core.Protos.DiaMessage;
import org.dianna.core.Protos.DiaMessage.Builder;
import org.dianna.core.message.Message;

import com.google.protobuf.ByteString;

public class MessageSerializer {

	public byte[] serialize(Message message) {
		Builder builder = DiaMessage.newBuilder();
		builder.setType(DiaMessage.DiaMessageType.valueOf(message.getType().name()));

		byte[] content = message.serialize();
		if (content != null) {
			builder.setContent(ByteString.copyFrom(content));
		}
		return builder.build().toByteArray();
	}

}
