package org.dianna.core.crypto;

import java.util.List;

import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;

import com.google.bitcoin.core.Sha256Hash;
import com.google.common.collect.Lists;

public class HashUtil {

	public static Sha256Hash getHash(DomainTransaction tx) {
		StringBuilder string = new StringBuilder();
		string.append(tx.getPrevTransaction()).append("\n");
		string.append(tx.getDomain()).append("\n");
		string.append(tx.getValue()).append("\n");
		string.append(tx.getFeeTransaction()).append("\n");
		string.append(tx.getNextPubkey()).append("\n");
		string.append(tx.getSignature());

		return Sha256Hash.createDouble(string.toString().getBytes());
	}

	public static List<Sha256Hash> buildMerkleTree(List<DomainTransaction> transactions) {
		// TODO implement merkle tree computation
		return Lists.newArrayList();
	}

	public static Sha256Hash getHash(DiannaBlock block) {
		StringBuilder string = new StringBuilder();
		string.append(block.getPrevBlockHash());
		string.append(block.getTimestamp().getMillis()).append("\n");
		string.append(block.getNamespace()).append("\n");
		string.append(block.getPrice().floatValue()).append("\n");
		string.append(block.getMerkleRootHash()).append("\n");

		return Sha256Hash.createDouble(string.toString().getBytes());
	}
}
