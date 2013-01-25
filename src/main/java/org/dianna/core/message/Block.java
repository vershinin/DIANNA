package org.dianna.core.message;

import java.math.BigDecimal;
import java.util.List;

import org.dianna.core.Transaction;
import org.joda.time.DateTime;

import com.google.bitcoin.core.Sha256Hash;

public class Block extends Payload {

	// Header
	private Integer version;
	private Sha256Hash prevBlockHash;
	private DateTime timestamp;
	private Integer namespace;
	private BigDecimal price;
	private Sha256Hash merkleRootHash;

	private List<Transaction> transactions;

	// AUX
	private Sha256Hash parentBlockHash;
	private int coinbaseTxIndex;
	private List<byte[]> auxBranch;

	/*
	 * Getters and setters
	 */

	public Integer getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Sha256Hash getPrevBlockHash() {
		return prevBlockHash;
	}

	public void setPrevBlockHash(Sha256Hash prevBlockHash) {
		this.prevBlockHash = prevBlockHash;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getNamespace() {
		return namespace;
	}

	public void setNamespace(int namespace) {
		this.namespace = namespace;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Sha256Hash getMerkleRootHash() {
		return merkleRootHash;
	}

	public void setMerkleRootHash(Sha256Hash merkleRootHash) {
		this.merkleRootHash = merkleRootHash;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	@Override
	public String toString() {
		// TODO as json format
		return super.toString();
	}

	public Sha256Hash getParentBlockHash() {
		return parentBlockHash;
	}

	public void setParentBlockHash(Sha256Hash parentBlockHash) {
		this.parentBlockHash = parentBlockHash;
	}

	public int getCoinbaseTxIndex() {
		return coinbaseTxIndex;
	}

	public void setCoinbaseTxIndex(int coinbaseTxIndex) {
		this.coinbaseTxIndex = coinbaseTxIndex;
	}

	public List<byte[]> getAuxBranch() {
		return auxBranch;
	}

	public void setAuxBranch(List<byte[]> auxBranch) {
		this.auxBranch = auxBranch;
	}
}
