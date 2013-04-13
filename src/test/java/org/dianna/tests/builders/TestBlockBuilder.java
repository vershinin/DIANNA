package org.dianna.tests.builders;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.joda.time.DateTime;

import com.google.bitcoin.core.Sha256Hash;

public class TestBlockBuilder {
	private DiannaBlock block;

	public TestBlockBuilder() {
		createBlock();
	}

	private void createBlock() {
		block = new DiannaBlock();
		block.setTransactions(new ArrayList<DomainTransaction>(5));
		block.setAuxBranch(new ArrayList<Pair<Sha256Hash, Sha256Hash>>(5));
	}

	public TestBlockBuilder withHash(Sha256Hash hash) {
		block.setHash(hash);
		return this;
	}

	public TestBlockBuilder withTimestamp(DateTime timestamp) {
		block.setTimestamp(timestamp);
		return this;
	}

	public TestBlockBuilder withNamespace(Integer namespace) {
		block.setNamespace(namespace);
		return this;
	}

	public TestBlockBuilder withPrice(BigDecimal price) {
		block.setPrice(price);
		return this;
	}

	public TestBlockBuilder withPerviousBlockHash(Sha256Hash hash) {
		block.setPrevBlockHash(hash);
		return this;
	}

	public TestBlockBuilder withMerkleRootHash(Sha256Hash hash) {
		block.setMerkleRootHash(hash);
		return this;
	}

	public TestBlockBuilder withParentBlockHash(Sha256Hash hash) {
		block.setParentBlockHash(hash);
		return this;
	}

	public TestBlockBuilder withCoinbaseTxId(int index) {
		block.setCoinbaseTxIndex(index);
		return this;
	}

	public TestBlockBuilder addAuxBranch(Pair<Sha256Hash, Sha256Hash> hashPair) {
		block.getAuxBranch().add(hashPair);
		return this;
	}

	public TestBlockBuilder addTransaction(DomainTransaction transaction) {
		block.getTransactions().add(transaction);
		return this;
	}

	public DiannaBlock build() {
		DiannaBlock newBlock = block;
		createBlock();
		return newBlock;
	}
}
