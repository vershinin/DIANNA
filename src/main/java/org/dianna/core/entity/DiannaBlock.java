package org.dianna.core.entity;

import java.math.BigDecimal;
import java.util.List;

import org.dianna.core.crypto.HashUtil;
import org.joda.time.DateTime;

import com.google.bitcoin.core.Sha256Hash;

public class DiannaBlock extends Entity {

	// Header
	private Sha256Hash hash;
	private Sha256Hash prevBlockHash;
	private DateTime timestamp;
	private Integer namespace;
	private BigDecimal price;
	private Sha256Hash merkleRootHash;

	private List<DomainTransaction> transactions;

	// AUX
	private Sha256Hash parentBlockHash;
	private int coinbaseTxIndex;
	private List<Sha256Hash> auxBranch;

	/*
	 * Getters and setters
	 */

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

	public List<DomainTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<DomainTransaction> transactions) {
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

	public List<Sha256Hash> getAuxBranch() {
		return auxBranch;
	}

	public void setAuxBranch(List<Sha256Hash> auxBranch) {
		this.auxBranch = auxBranch;
	}

	public Sha256Hash getHash() {
		if(hash==null){
			hash = HashUtil.getHash(this);
		}
		return hash;
	}

	public void setHash(Sha256Hash hash) {
		this.hash = hash;
	}

}
