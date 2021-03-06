package org.dianna.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;

import com.google.bitcoin.core.Sha256Hash;

public class HashUtil {

	public static Sha256Hash calculateHash(DomainTransaction tx) {
		StringBuilder string = new StringBuilder();
		string.append(tx.getPrevTransaction()).append("\n");
		string.append(tx.getDomain()).append("\n");
		string.append(tx.getValue()).append("\n");
		string.append(tx.getFeeTransaction()).append("\n");
		string.append(tx.getNextPubkey()).append("\n");
		string.append(tx.getSignature());

		return Sha256Hash.createDouble(string.toString().getBytes());
	}

	public static MerkleTree buildMerkleTree(List<DomainTransaction> transactions) {
		MerkleTree merkleTree = new MerkleTree();
		List<Sha256Hash> hashList = new ArrayList<Sha256Hash>(transactions.size());
		for (DomainTransaction tx : transactions) {
			hashList.add(HashUtil.calculateHash(tx));
		}
		merkleTree.buildTree(hashList);
		return merkleTree;
	}

	public static Sha256Hash calculateHash(DiannaBlock block) {
		StringBuilder string = new StringBuilder();
		string.append(block.getPrevBlockHash());
		string.append(block.getTimestamp().getMillis()).append("\n");
		string.append(block.getNamespace()).append("\n");
		string.append(block.getPrice().floatValue()).append("\n");
		string.append(block.getMerkleRootHash()).append("\n");

		return Sha256Hash.createDouble(string.toString().getBytes());
	}

	public static Sha256Hash createDoubleHash(Sha256Hash leftHash, Sha256Hash rightHash) {
		return Sha256Hash.createDouble(ArrayUtils.addAll(leftHash.getBytes(), rightHash.getBytes()));
	}
}
