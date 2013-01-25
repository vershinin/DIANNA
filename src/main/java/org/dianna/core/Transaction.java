/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dianna.core;

import java.io.IOException;
import java.io.InputStream;

import org.dianna.core.utils.DiannaUtils;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 *
 * @author pentarh
 */
public class Transaction {
    private static final int TRANSACTION_VERSION=1;

    /**
     * Transaction version. Clients catching unknown version should decline transaction.
     */
    private int version;

    /**
     * Hash of referenced bitcoin transaction with fee
     */
    private Sha256Hash feeTransaction;

    /**
     * Hash of previous DIANNA transaction operating with current domain. NULL for domain creation, domain update otherwise.
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

    public Transaction(byte[] domain, byte[] value, Sha256Hash feeTransaction, Sha256Hash prevTransaction, byte[] nextPubkey) {
        this.version=TRANSACTION_VERSION;
        this.domain=domain;
        this.value=value;
        this.feeTransaction=feeTransaction;
        this.prevTransaction=prevTransaction;
        this.nextPubkey=nextPubkey;
    }

    /**
     * This is used to get Transaction object from protobuf serialized stream
     * input stream
     * @param stream
     * @throws org.dianna.core.DProtocolException
     */
    public Transaction(InputStream stream) throws DProtocolException {
        Protos.DiaDomainTransaction tx;
        try {
            tx=Protos.DiaDomainTransaction.parseFrom(stream);
        } catch (IOException e) {
            throw new DProtocolException(e.getMessage());
        }
        populateFromProto(tx);
    }

    /**
     * This is used to get Transaction object from protobuf serialized byte array
     * @param serializedBytes
     * @throws DProtocolException
     */
    public Transaction(byte[] serializedBytes) throws DProtocolException {
        Protos.DiaDomainTransaction tx;
        try {
            tx=Protos.DiaDomainTransaction.parseFrom(serializedBytes);
        } catch (InvalidProtocolBufferException e) {
            throw new DProtocolException(e.getMessage());
        }
        populateFromProto(tx);
    }

    /**
     * Wrapper to protobuf DiaDomainTransaction class
     * @param tx
     * @throws org.dianna.core.DProtocolException
     */
    private void populateFromProto(Protos.DiaDomainTransaction tx) throws DProtocolException {
        version=TRANSACTION_VERSION;
        if (tx.hasPrevTransaction()) {
            if (tx.getPrevTransaction().size()!=32) {
                throw new DProtocolException("prevTransaction.length()!=32");
            }
            prevTransaction=new Sha256Hash(tx.getPrevTransaction().toByteArray());
        } else {
            this.prevTransaction=null;
        }
        if (tx.getFeeTransaction().size()!=32) {
            throw new DProtocolException("feeTransaction.length()!=32");
        }
        // TODO: add length checks
        this.feeTransaction=new Sha256Hash(tx.getFeeTransaction().toByteArray());
        this.domain=tx.getDomain().toByteArray();
        this.value=tx.getValue().toByteArray();
        this.nextPubkey=tx.getNextPubkey().toByteArray();
        this.signature=tx.getSignature().toByteArray();
        verifyProtoRestrictions();
    }

    /**
     * Get simplified transaction version serialized to byte array for signing purposes
     * @return
     */
    public byte[] getSimpleSerialized() {
        Protos.DiaDomainTransactionSimple.Builder dBuilder=Protos.DiaDomainTransactionSimple.newBuilder()
                .setVersion(version)
                .setFeeTransaction(ByteString.copyFrom(feeTransaction.getBytes()))
                .setDomain(ByteString.copyFrom(domain))
                .setValue(ByteString.copyFrom(value))
                .setNextPubkey(ByteString.copyFrom(nextPubkey));
        if (prevTransaction!=null) {
            dBuilder.setPrevTransaction(ByteString.copyFrom(prevTransaction.getBytes()));
        }

        return dBuilder.build().toByteArray();
    }

    public byte[] getSerialized() {
         Protos.DiaDomainTransaction.Builder dBuilder=Protos.DiaDomainTransaction.newBuilder()
                .setVersion(version)
                .setFeeTransaction(ByteString.copyFrom(feeTransaction.getBytes()))
                .setDomain(ByteString.copyFrom(domain))
                .setValue(ByteString.copyFrom(value))
                .setNextPubkey(ByteString.copyFrom(nextPubkey))
                .setSignature(ByteString.copyFrom(signature));
        if (prevTransaction!=null) {
            dBuilder.setPrevTransaction(ByteString.copyFrom(prevTransaction.getBytes()));
        }

        return dBuilder.build().toByteArray();
    }

    /**
     * Sign transaction with provided key
     * @param key
     */
    public void Sign(ECKey key) {
        // Signing double hash of serialized simplified transaction
        byte[] payload=DiannaUtils.doubleDigest(getSimpleSerialized());
        signature=key.sign(payload);
    }

    /**
     * Ensure we had got valid data from serialized source. Various sizes checks here
     * @throws DProtocolException
     */
    private void verifyProtoRestrictions() throws DProtocolException {
        if (version <=0 || version > TRANSACTION_VERSION) {
            throw new DProtocolException("Unknown transaction version: "+version);
        }
        if (feeTransaction == null) {
            throw new DProtocolException("Empty fee transaction: NULL");
        }
        if (domain == null) {
            throw new DProtocolException("Domain is empty: NULL");
        }
        if (domain.length<=0 || domain.length > Constants.MAX_DOMAIN_LENGTH) {
            throw new DProtocolException("Domain length out of bounds: "+domain.length);
        }
        if (value == null) {
            throw new DProtocolException("Value is empty: NULL");
        }
        if (value.length<=0 || value.length > Constants.MAX_VALUE_LENGTH) {
            throw new DProtocolException("Value length out of bounds: "+value.length);
        }
        if (nextPubkey==null){
            throw new DProtocolException("Nextpubkey is empty: NULL");
        }
        // TODO: nextPubkey length check??
        if (signature == null) {
            throw new DProtocolException("Signature is empty: NULL");
        }
    }

    @Override
    public String toString() {
        String s="";
        s+="Transaction: \n";
        s+="   version: " + version + "\n";
        s+="   feeTransaction: " + feeTransaction.toString() + "\n";
        s+="   prevTransaction: ";
        if (prevTransaction==null) {
            s+="NULL\n";
        } else {
            s+=prevTransaction.toString() + "\n";
        }
        // Try to guess string from domain and value bytes. Its most likely a string
        s+="   domain: " + ByteString.copyFrom(domain).toStringUtf8() + "\n";
        s+="   value: " + DiannaUtils.strTruncate(ByteString.copyFrom(value).toStringUtf8(),60) + " (" + value.length + " bytes)" + "\n";
        s+="   nextPubkey: " + DiannaUtils.bytesToHexString(nextPubkey) + "\n";
        s+="   signature: " + DiannaUtils.bytesToHexString(signature) + "\n";
        return s;
    }

}
