package org.dianna.core.serialization.payload;

import org.dianna.core.Protos.DiaDomainTransaction;
import org.dianna.core.Protos.DiaDomainTransaction.Builder;
import org.dianna.core.Transaction;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.Payload;

import com.google.bitcoin.core.Sha256Hash;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class TransactionSerializer implements PayloadSerializer {

	@Override
	public byte[] serialize(Payload message) {
		return serialize((Transaction) message).toByteArray();
	}

	@Override
	public Payload deserialize(byte[] byteArray) {
		DiaDomainTransaction transaction = null;
		try {
			transaction = DiaDomainTransaction.parseFrom(byteArray);
		} catch (InvalidProtocolBufferException e) {
			return null; // TODO log
		}
		transaction.getDomain();
		transaction.getValue();
		transaction.getVersion();
		transaction.getNextPubkey();
		transaction.getPrevTransaction();
		transaction.getSignature();

		return null;
	}

	DiaDomainTransaction serialize(Transaction transaction) {
		Builder builder = DiaDomainTransaction.newBuilder();
		builder.setVersion(transaction.getVersion())
				.setFeeTransaction(ByteString.copyFrom(transaction.getFeeTransaction().getBytes()))
				.setDomain(ByteString.copyFrom(transaction.getDomain()))
				.setValue(ByteString.copyFrom(transaction.getValue()))
				.setNextPubkey(ByteString.copyFrom(transaction.getNextPubkey()))
				.setSignature(ByteString.copyFrom(transaction.getSignature()));

		Sha256Hash prevTransaction = transaction.getPrevTransaction();
		if (prevTransaction != null) {
			builder.setPrevTransaction(ByteString.copyFrom(prevTransaction.getBytes()));
		}

		return builder.build();
	}

	@Override
	public MessageType getType() {
		return MessageType.TRANSACTION;
	}

}
