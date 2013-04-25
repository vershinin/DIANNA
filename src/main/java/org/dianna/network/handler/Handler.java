package org.dianna.network.handler;

import net.tomp2p.peers.PeerAddress;

import org.dianna.core.exception.InvalidMessageException;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;

public interface Handler {
	public Message handle(PeerAddress peer, Message message) throws InvalidMessageException;

	public MessageType getType();
}
