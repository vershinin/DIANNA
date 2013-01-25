/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dianna.core;

import java.io.IOException;
import java.io.InputStream;

import org.dianna.core.message.Message;
import org.dianna.core.message.Payload;
import org.dianna.core.utils.DiannaUtils;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 
 * @author pentarh
 */
public class Transaction extends Payload {
	private static final int TRANSACTION_VERSION = 1;

	/**
	 * Transaction version. Clients catching unknown version should decline
	 * transaction.
	 */
	private Integer version;

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
	private byte[] domain;

	/**
	 * Value to assign to this domain
	 */
	private byte[] value;

	/**
	 * Public key allowed for next operation with this domain
	 */
	private byte[] nextPubkey;

	/**
	 * Transaction signature
	 */
	private byte[] signature;

	public Transaction(byte[] domain, byte[] value, Sha256Hash feeTransaction, Sha256Hash prevTransaction,
			byte[] nextPubkey) {
		this.setVersion(TRANSACTION_VERSION);
		this.setDomain(domain);
		this.setValue(value);
		this.setFeeTransaction(feeTransaction);
		this.setPrevTransaction(prevTransaction);
		this.setNextPubkey(nextPubkey);
	}

	/**
	 * This is used to get Transaction object from protobuf serialized stream
	 * input stream
	 * 
	 * @param stream
	 * @throws org.dianna.core.DProtocolException
	 */
	public Transaction(InputStream stream) throws DProtocolException {
		Protos.DiaDomainTransaction tx;
		try {
			tx = Protos.DiaDomainTransaction.parseFrom(stream);
		} catch (IOException e) {
			throw new DProtocolException(e.getMessage());
		}
		populateFromProto(tx);
	}

	/**
	 * This is used to get Transaction object from protobuf serialized byte
	 * array
	 * 
	 * @param serializedBytes
	 * @throws DProtocolException
	 */
	public Transaction(byte[] serializedBytes) throws DProtocolException {
		Protos.DiaDomainTransaction tx;
		try {
			tx = Protos.DiaDomainTransaction.parseFrom(serializedBytes);
		} catch (InvalidProtocolBufferException e) {
			throw new DProtocolException(e.getMessage());
		}
		populateFromProto(tx);
	}

	/**
	 * Wrapper to protobuf DiaDomainTransaction class
	 * 
	 * @param tx
	 * @throws org.dianna.core.DProtocolException
	 */
	private void populateFromProto(Protos.DiaDomainTransaction tx) throws DProtocolException {
		setVersion(TRANSACTION_VERSION);
		if (tx.hasPrevTransaction()) {
			if (tx.getPrevTransaction().size() != 32) {
				throw new DProtocolException("prevTransaction.length()!=32");
			}
			setPrevTransaction(new Sha256Hash(tx.getPrevTransaction().toByteArray()));
		} else {
			this.setPrevTransaction(null);
		}
		if (tx.getFeeTransaction().size() != 32) {
			throw new DProtocolException("feeTransaction.length()!=32");
		}
		// TODO: add length checks
		this.setFeeTransaction(new Sha256Hash(tx.getFeeTransaction().toByteArray()));
		this.setDomain(tx.getDomain().toByteArray());
		this.setValue(tx.getValue().toByteArray());
		this.setNextPubkey(tx.getNextPubkey().toByteArray());
		this.setSignature(tx.getSignature().toByteArray());
		verifyProtoRestrictions();
	}

	/**
	 * Get simplified transaction version serialized to byte array for signing
	 * purposes
	 * 
	 * @return
	 */
	public byte[] getSimpleSerialized() {
		Protos.DiaDomainTransactionSimple.Builder dBuilder = Protos.DiaDomainTransactionSimple.newBuilder()
				.setVersion(getVersion()).setFeeTransaction(ByteString.copyFrom(getFeeTransaction().getBytes()))
				.setDomain(ByteString.copyFrom(getDomain())).setValue(ByteString.copyFrom(getValue()))
				.setNextPubkey(ByteString.copyFrom(getNextPubkey()));
		if (getPrevTransaction() != null) {
			dBuilder.setPrevTransaction(ByteString.copyFrom(getPrevTransaction().getBytes()));
		}

		return dBuilder.build().toByteArray();
	}

	public byte[] getSerialized() {
		Protos.DiaDomainTransaction.Builder dBuilder = Protos.DiaDomainTransaction.newBuilder().setVersion(getVersion())
				.setFeeTransaction(ByteString.copyFrom(getFeeTransaction().getBytes()))
				.setDomain(ByteString.copyFrom(getDomain())).setValue(ByteString.copyFrom(getValue()))
				.setNextPubkey(ByteString.copyFrom(getNextPubkey())).setSignature(ByteString.copyFrom(getSignature()));
		if (getPrevTransaction() != null) {
			dBuilder.setPrevTransaction(ByteString.copyFrom(getPrevTransaction().getBytes()));
		}

		return dBuilder.build().toByteArray();
	}

	/**
	 * Sign transaction with provided key
	 * 
	 * @param key
	 */
	public void Sign(ECKey key) {
		// Signing double hash of serialized simplified transaction
		byte[] payload = DiannaUtils.doubleDigest(getSimpleSerialized());
		setSignature(key.sign(payload));
	}

	/**
	 * Ensure we had got valid data from serialized source. Various sizes checks
	 * here
	 * 
	 * @throws DProtocolException
	 */
	private void verifyProtoRestrictions() throws DProtocolException {
		if (getVersion() <= 0 || getVersion() > TRANSACTION_VERSION) {
			throw new DProtocolException("Unknown transaction version: " + getVersion());
		}
		if (getFeeTransaction() == null) {
			throw new DProtocolException("Empty fee transaction: NULL");
		}
		if (getDomain() == null) {
			throw new DProtocolException("Domain is empty: NULL");
		}
		if (getDomain().length <= 0 || getDomain().length > Constants.MAX_DOMAIN_LENGTH) {
			throw new DProtocolException("Domain length out of bounds: " + getDomain().length);
		}
		if (getValue() == null) {
			throw new DProtocolException("Value is empty: NULL");
		}
		if (getValue().length <= 0 || getValue().length > Constants.MAX_VALUE_LENGTH) {
			throw new DProtocolException("Value length out of bounds: " + getValue().length);
		}
		if (getNextPubkey() == null) {
			throw new DProtocolException("Nextpubkey is empty: NULL");
		}
		// TODO: nextPubkey length check??
		if (getSignature() == null) {
			throw new DProtocolException("Signature is empty: NULL");
		}
	}

	@Override
	public String toString() {
		String s = "";
		s += "Transaction: \n";
		s += "   version: " + getVersion() + "\n";
		s += "   feeTransaction: " + getFeeTransaction().toString() + "\n";
		s += "   prevTransaction: ";
		if (getPrevTransaction() == null) {
			s += "NULL\n";
		} else {
			s += getPrevTransaction().toString() + "\n";
		}
		// Try to guess string from domain and value bytes. Its most likely a
		// string
		s += "   domain: " + ByteString.copyFrom(getDomain()).toStringUtf8() + "\n";
		s += "   value: " + DiannaUtils.strTruncate(ByteString.copyFrom(getValue()).toStringUtf8(), 60) + " ("
				+ getValue().length + " bytes)" + "\n";
		s += "   nextPubkey: " + DiannaUtils.bytesToHexString(getNextPubkey()) + "\n";
		s += "   signature: " + DiannaUtils.bytesToHexString(getSignature()) + "\n";
		return s;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

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

	public byte[] getDomain() {
		return domain;
	}

	public void setDomain(byte[] domain) {
		this.domain = domain;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public byte[] getNextPubkey() {
		return nextPubkey;
	}

	public void setNextPubkey(byte[] nextPubkey) {
		this.nextPubkey = nextPubkey;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

}
