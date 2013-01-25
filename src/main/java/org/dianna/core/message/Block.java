package org.dianna.core.message;

import java.math.BigDecimal;
import java.util.List;

import org.dianna.core.Protos;
import org.dianna.core.Protos.DiaBlock;
import org.dianna.core.Protos.DiaBlockHeader;
import org.dianna.core.Transaction;
import org.joda.time.DateTime;

import com.google.bitcoin.core.Sha256Hash;
import com.google.protobuf.InvalidProtocolBufferException;

public class Block extends Message {

	private int version;
	private Sha256Hash prevBlockHash;
	private DateTime timestamp;
	private int namespace;
	private BigDecimal price;
	private Sha256Hash merkleRootHash;

	private List<Transaction> transactions;

	public Block(byte[] msg) {
		super(msg);
	}

	@Override
	protected void parse() {
		try {
			DiaBlock block = Protos.DiaBlock.parseFrom(msg);
			populateHeader(block.getHeader());
		} catch (InvalidProtocolBufferException e) {

		}
	}

	private void populateHeader(DiaBlockHeader header) {
		this.setVersion(header.getVersion());
		this.setPrevBlockHash(new Sha256Hash(header.getPrevBlockHash().toByteArray()));
		this.setTimestamp(new DateTime(header.getTimestamp()));
		this.setNamespace(header.getNamespace());
		this.setPrice(new BigDecimal(header.getPrice()));
		this.setMerkleRootHash(new Sha256Hash(header.getMerkleRoot().toByteArray()));
	}

	/*
	 * Getters and setters
	 */

	public int getVersion() {
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
		// TODO implement
		return super.toString();
	}
}
