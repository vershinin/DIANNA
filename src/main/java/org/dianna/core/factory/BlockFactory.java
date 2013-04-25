package org.dianna.core.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.dianna.core.Dianna;
import org.dianna.core.crypto.HashUtil;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.settings.DiannaSettings;
import org.dianna.core.store.BlockStore;
import org.dianna.core.store.BlockStore.BlockStoreListener;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.Sha256Hash;

/**
 * This class holds block for building process
 * 
 * @author ivan
 * 
 */
public class BlockFactory implements BlockStoreListener {
	private static Logger logger = LoggerFactory.getLogger(Dianna.class);

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
		newBlock.setMerkleRootHash(HashUtil.buildMerkleTree(transactions).getRoot());

		newBlock.setHash(HashUtil.calculateHash(newBlock));

		logger.info("New transaction added. Recalculated hash is {}", newBlock.getHash());
	}

	public synchronized void setAuxData(Sha256Hash parentHash, List<Sha256Hash> auxBranch) {
		//newBlock.setAuxBranch(auxBranch);
	}

	@Override
	public void onCorrectBlockRecieved(DiannaBlock block) {
		newBlock.setPrevBlockHash(block.getHash());
		newBlock.setHash(HashUtil.calculateHash(newBlock));
		logger.info("New block {} recieved. Recalculated hash is {}", block.getHash(), newBlock.getHash());
	}

	public Sha256Hash getNewBlockHash() {
		return newBlock.getHash();
	}

	public DiannaBlock build() {
		return newBlock;
	}

}
