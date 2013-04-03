package org.dianna.core.store;

import java.util.Map;

import org.dianna.core.entity.Block;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.validators.BlockValidator;

import com.google.common.collect.Maps;

public class BlockStore {
	private BlockValidator validator;
	private Map<String, Block> data = Maps.newHashMap();

	public BlockStore(BlockValidator validator) {
		this.validator = validator;
	}

	public void addBlock(Block block) throws ValidationException {
		validator.validateBlock(block);
		data.put(block.getHash().toString(), block);
	}

	public void getBlock(String hash) {
		data.get(hash);
	}
}
