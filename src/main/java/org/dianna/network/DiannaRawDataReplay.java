package org.dianna.network;

import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.RawDataReply;

import org.dianna.core.message.Message;
import org.dianna.core.serialization.MessageSerializer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;


/**
 * This class is required to serialize/deserialize
 * 
 * @author ivan
 * 
 */
public class DiannaRawDataReplay implements RawDataReply {
	private static final int BUFF_SIZE = 1024;

	private MessageSerializer serializer;

	private MessageHandler messageHandler;

	@Override
	public ChannelBuffer reply(PeerAddress sender, ChannelBuffer requestBuffer) throws Exception {
		Message message = serializer.deserialize(requestBuffer.array());
		Message responseMessage = messageHandler.handleMessage(sender, message);
		if (responseMessage == null) {
			return null;
		}
		return ChannelBuffers.wrappedBuffer(serializer.serialize(responseMessage));
	}

	public void setSerializer(MessageSerializer serializer) {
		this.serializer = serializer;
	}

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

}
