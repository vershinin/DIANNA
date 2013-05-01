package org.dianna.core.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dianna.core.Dianna;
import org.dianna.core.chain.DiannaBlockChain;
import org.dianna.core.chain.IncomingBlockEvent;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.settings.DiannaSettings;
import org.dianna.core.utils.HashUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.Sha256Hash;
import com.google.common.eventbus.Subscribe;

/**
 * This class holds block for building process
 * 
 * @author ivan
 * 
 */
public class BlockFactory {
	private static Logger logger = LoggerFactory.getLogger(Dianna.class);

	private DiannaSettings settings;
	private DiannaBlockChain blockChain;

	private DiannaBlock newBlock = null;

	public BlockFactory(DiannaSettings settings) {
		this.settings = settings;
		createNewBlock();
	}

	public synchronized void addTransaction(DomainTransaction domainTransaction) {
		logger.info("New transaction added.");
		List<DomainTransaction> transactions = newBlock.getTransactions();
		transactions.add(domainTransaction);
		newBlock.setMerkleRootHash(HashUtil.buildMerkleTree(transactions).getRoot());
		recalculateHash();
	}

	public synchronized void setAuxData(Sha256Hash parentHash, List<Sha256Hash> auxBranch) {
		// newBlock.setAuxBranch(auxBranch);
	}

	@Subscribe
	public synchronized void handleNewIncoimingBlock(IncomingBlockEvent event) {
		DiannaBlock incomingBlock = event.getBlock();
		logger.info("New block {} recieved.", incomingBlock.getHash());
		newBlock.setHeight(blockChain.getHeight());
		newBlock.setPrevBlockHash(incomingBlock.getHash());
		newBlock.setTimestamp(DateTime.now());
		newBlock.setPrice(blockChain.getPriceForBlock(newBlock));
		recalculateHash();
	}

	public Sha256Hash getCurrentBlockHash() {
		return newBlock.getHash();
	}

	public synchronized DiannaBlock build() {
		DiannaBlock readyBlock = newBlock;
		createNewBlock();
		readyBlock.setTransactions(Collections.unmodifiableList(readyBlock.getTransactions()));
		return readyBlock;
	}

	private void recalculateHash() {
		newBlock.setHash(HashUtil.calculateHash(newBlock));
		logger.info("Recalculated hash is {}", newBlock.getHash());
	}

	private void createNewBlock() {
		newBlock = new DiannaBlock();
		Integer height = blockChain.getHeight();
		newBlock.setHeight(height);

		if (height != 0) {
			newBlock.setPrevBlockHash(blockChain.getLatestBlock().getHash());
		}

		newBlock.setNamespace(settings.getNamespace());
		newBlock.setTransactions(new ArrayList<DomainTransaction>());
		newBlock.setCoinbaseTxIndex(settings.getCoinbaseTxIndex());
		newBlock.setTimestamp(DateTime.now());

		BigDecimal price = blockChain.getPriceForBlock(newBlock);
		if (height == 0) { // Namespace initiator should pay twice
			newBlock.setPrice(price.multiply(new BigDecimal(2)));
		} else {
			newBlock.setPrice(price);
		}
	}

	public String getCurrentTarget() {
		return null;
	}
}
