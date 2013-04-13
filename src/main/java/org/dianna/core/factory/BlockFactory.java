package org.dianna.core.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.dianna.core.DiannaSettings;
import org.dianna.core.crypto.HashUtil;
import org.dianna.core.crypto.MerkleTree;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.store.BlockStore;
import org.dianna.core.store.BlockStore.BlockStoreListener;
import org.joda.time.DateTime;

import com.google.bitcoin.core.Sha256Hash;

/**
 * This class holds block for building process
 * 
 * @author ivan
 * 
 */
public class BlockFactory implements BlockStoreListener {
	private DiannaSettings settings;
	private BlockStore blockStore;

	private DiannaBlock newBlock = null;

	public BlockFactory(DiannaSettings settings) {
		this.settings = settings;
		newBlock = buildNewBlock();
	}

	private DiannaBlock buildNewBlock() {
		DiannaBlock b = new DiannaBlock();
		b.setNamespace(settings.getNamespace());
		b.setTransactions(new ArrayList<DomainTransaction>());
		b.setCoinbaseTxIndex(settings.getCoinbaseTxIndex());
		b.setTimestamp(DateTime.now());
		b.setPrice(BigDecimal.ONE);
		return b;
	}

	public synchronized void addTransaction(DomainTransaction domainTransaction) {
		List<DomainTransaction> transactions = newBlock.getTransactions();
		transactions.add(domainTransaction);

		MerkleTree merkleTree = new MerkleTree();
		for (DomainTransaction tx : transactions) {
			merkleTree.addLeaf(tx.getHash());
		}

		newBlock.setMerkleRootHash(merkleTree.getRoot());
	}

	public synchronized void setAuxData(Sha256Hash parentHash, List<Sha256Hash> auxBranch) {
		newBlock.setAuxBranch(auxBranch);
	}

	@Override
	public void onCorrectBlockRecieved(DiannaBlock block) {
		newBlock.setPrevBlockHash(block.getHash());
		newBlock.setHash(HashUtil.getHash(newBlock));
	}

	public Sha256Hash getNewBlockHash() {
		return newBlock.getHash();
	}

	public DiannaBlock build() {
		return newBlock;
	}

}
