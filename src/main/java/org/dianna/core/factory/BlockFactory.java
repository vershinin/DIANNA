package org.dianna.core.factory;

import org.dianna.core.message.payload.Block;

public class BlockFactory {
	public Block buildNewBlock(){
		Block b = new Block();
		//b.setAuxBranch();
		return b;
	}
}
