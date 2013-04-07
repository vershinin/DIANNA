package org.dianna.core.message;

import org.dianna.core.entity.DiannaBlock;

public class BlockMessage extends Message {

	private DiannaBlock block;

	public BlockMessage() {
		this(null);
	}

	public BlockMessage(DiannaBlock block) {
		super(MessageType.BLOCK);
		this.setBlock(block);
	}

	public DiannaBlock getBlock() {
		return block;
	}

	public void setBlock(DiannaBlock block) {
		this.block = block;
	}

}
