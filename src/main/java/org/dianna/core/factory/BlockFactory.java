package org.dianna.core.factory;

import java.util.ArrayList;
import java.util.List;

import org.dianna.core.DiannaSettings;
import org.dianna.core.crypto.HashUtil;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.store.BlockStore;
import org.dianna.core.store.BlockStore.BlockStoreListener;
import org.joda.time.DateTime;

import com.google.bitcoin.core.Sha256Hash;

public class BlockFactory implements BlockStoreListener {
	private DiannaSettings settings;
	private BlockStore blockStore;

	private DiannaBlock newBlock = null;

	public BlockFactory(DiannaSettings settings) {
		this.settings = settings;
	}

	public DiannaBlock buildNewBlock() {
		DiannaBlock b = new DiannaBlock();
		b.setNamespace(settings.getNamespace());
		b.setTransactions(new ArrayList<DomainTransaction>());
		b.setCoinbaseTxIndex(settings.getCoinbaseTxIndex());
		b.setTimestamp(DateTime.now());
		return b;
	}

	public synchronized void addTransaction(DomainTransaction domainTransaction) {
		newBlock.getTransactions().add(domainTransaction);
		newBlock.setMerkleRootHash(null);
		newBlock.setHash(HashUtil.getHash(newBlock));

		HashUtil.buildMerkleTree(newBlock.getTransactions());
	}

	public synchronized void setAuxData(Sha256Hash parentHash, List<Sha256Hash> auxBranch) {
		newBlock.setAuxBranch(auxBranch);
	}

	@Override
	public void onCorrectBlockRecieved(DiannaBlock block) {
		newBlock.setPrevBlockHash(block.getHash());
		newBlock.setHash(HashUtil.getHash(newBlock));
	}

	public void getNewBlockHash() {
		// TODO Auto-generated method stub

	}

}
