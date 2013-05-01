package org.dianna.core.chain;

import org.dianna.core.entity.DiannaBlock;

public class IncomingBlockEvent {
	private final DiannaBlock block;

	public IncomingBlockEvent(DiannaBlock block) {
		this.block = block;
	}

	public DiannaBlock getBlock() {
		return block;
	}
}
