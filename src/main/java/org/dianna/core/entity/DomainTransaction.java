/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dianna.core.entity;

import org.dianna.core.crypto.HashUtil;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.ECKey.ECDSASignature;
import com.google.bitcoin.core.Sha256Hash;

/**
 * 
 * @author pentarh
 * @author ivan
 */
public class DomainTransaction extends Entity {
	private Sha256Hash hash;

	/**
	 * Hash of referenced bitcoin transaction with fee
	 */
	private Sha256Hash feeTransaction;

	/**
	 * Hash of previous DIANNA transaction operating with current domain. NULL
	 * for domain creation, domain update otherwise.
	 */
	private Sha256Hash prevTransaction;

	/**
	 * Actual domain name to operate with
	 */
	private String domain;

	/**
	 * Value to assign to this domain
	 */
	private String value;

	/**
	 * Public key allowed for next operation with this domain
	 */
	private ECKey nextPubkey;

	/**
	 * Transaction signature
	 */
	private String signature;

	public Sha256Hash getFeeTransaction() {
		return feeTransaction;
	}

	public void setFeeTransaction(Sha256Hash feeTransaction) {
		this.feeTransaction = feeTransaction;
	}

	public Sha256Hash getPrevTransaction() {
		return prevTransaction;
	}

	public void setPrevTransaction(Sha256Hash prevTransaction) {
		this.prevTransaction = prevTransaction;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ECKey getNextPubkey() {
		return nextPubkey;
	}

	public void setNextPubkey(ECKey nextPubkey) {
		this.nextPubkey = nextPubkey;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Sha256Hash getHash() {
		return hash;
	}

	public void setHash(Sha256Hash hash) {
		this.hash = hash;
	}

}
