package org.dianna.core.store;

import java.util.List;
import java.util.Map;

import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.validators.BlockValidator;

import com.beust.jcommander.internal.Lists;
import com.google.bitcoin.core.Sha256Hash;
import com.google.common.collect.Maps;

public class BlockStore {

	private BlockValidator validator;
	private Map<String, DiannaBlock> data = Maps.newHashMap();
	private List<BlockStoreListener> listeners = Lists.newArrayList();

	public BlockStore(BlockValidator validator) {
		this.validator = validator;
	}

	public void addBlock(DiannaBlock block) throws ValidationException {
		validator.validateBlock(block);
		data.put(block.getHash().toString(), block);
		for (BlockStoreListener listener : listeners) {
			listener.updatePreviousBlockHash(block);
		}
	}

	public void getBlock(String hash) {
		data.get(hash);
	}

	public DiannaBlock getBlockByParent(Sha256Hash sha256Hash) {
		return null;
	}

	public void addBlockStoreListener(BlockStoreListener listener) {
		listeners.add(listener);
	}

	public void removeBlockStoreListener(BlockStoreListener listener) {
		listeners.remove(listener);
	}

	public interface BlockStoreListener {
		public void updatePreviousBlockHash(DiannaBlock block);

		void updatePreviousBlockHash(Sha256Hash prevBlockHash);
	}

	public DiannaBlock getLatestCheckpointBlock() {
		return null;
	}

	public DiannaBlock getLatestBlock() {
		return null;
	}

	public DiannaBlock getPreLastCheckpoint() {
		// TODO Auto-generated method stub
		return null;
	}
}
