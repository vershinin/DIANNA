package org.dianna.core.crypto;

import java.util.List;

import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;

import com.google.bitcoin.core.Sha256Hash;
import com.google.common.collect.Lists;

public class HashUtil {

	public static Sha256Hash getHash(DomainTransaction tx) {
		String json; // TODO
		return null;
	}

	public static List<Sha256Hash> buildMerkleTree(List<DomainTransaction> transactions) {
		// TODO implement merkle tree computation
		return Lists.newArrayList();
	}

	public static Sha256Hash getHash(DiannaBlock newBlock) {
		// TODO Auto-generated method stub
		return null;
	}

}
