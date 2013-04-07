package org.dianna.core.entity;

import java.util.List;

import com.google.bitcoin.core.Sha256Hash;

public class AuxData {
	private Sha256Hash parentBlockHash;
	private int coinbaseTxIndex;
	private List<Sha256Hash> auxMerkleBranch;

	public List<Sha256Hash> getAuxMerkleBranch() {
		return auxMerkleBranch;
	}

	public void setAuxMerkleBranch(List<Sha256Hash> auxMerkleBranch) {
		this.auxMerkleBranch = auxMerkleBranch;
	}

	public int getCoinbaseTxIndex() {
		return coinbaseTxIndex;
	}

	public void setCoinbaseTxIndex(int coinbaseTxIndex) {
		this.coinbaseTxIndex = coinbaseTxIndex;
	}

	public Sha256Hash getParentBlockHash() {
		return parentBlockHash;
	}

	public void setParentBlockHash(Sha256Hash parentBlockHash) {
		this.parentBlockHash = parentBlockHash;
	}
}
