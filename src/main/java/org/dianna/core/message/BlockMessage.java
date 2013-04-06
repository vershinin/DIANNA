package org.dianna.core.message;

import org.dianna.core.entity.DiannaBlock;

public class BlockMessage extends Message {

	public BlockMessage() {
		this(null);
	}

	public BlockMessage(DiannaBlock block) {
		super(MessageType.BLOCK);
		setPayload(block);
	}

}
