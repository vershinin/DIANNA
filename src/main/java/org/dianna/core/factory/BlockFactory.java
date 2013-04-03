package org.dianna.core.factory;

import org.dianna.core.entity.Block;

public class BlockFactory {
	public Block buildNewBlock(){
		Block b = new Block();
		//b.setAuxBranch();
		return b;
	}
}
