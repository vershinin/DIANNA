package org.dianna.tests.builders;

import java.math.BigDecimal;

import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.joda.time.DateTime;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;

public class TestDataFactory {
	public static final Sha256Hash PARENT_HASH = new Sha256Hash(
			"2b929f20cc6042612d431164063f31ec4c8c42ab7c7d8e92e23a1eb6e882d641");

	public static final Sha256Hash PREVIOUS_HASH = new Sha256Hash(
			"32181d0a7b5ea8ce70662dff058ca918e45670c4197b33f3eaf695a4cadf427a");

	public static final Sha256Hash MERKLE_ROOT_HASH = new Sha256Hash(
			"2f279cc3c9c49c4394f942134e9292f1dc44d7344a3bcb9555dc49c29a8b0a34");

	public static final Sha256Hash CORRECT_BLOCK_HASH = new Sha256Hash(
			"8288393494a63fd9d15aa952ff05c1ff8b94fab06596caa540e006805c5db0ae");

	// TRANSACTIONS
	public static final String PUB_KEY = "02f0f88fa05e7f6db4b26fef5af616b73322017d868cc7a631d39f0a4b3068c89e";
	public static final String SIGNATURE = "MEYCIQCdxzlTRAO+D9Xjg7auWdpPX/UsQLUUArWOJ7O3dPsalQIhANnxwKWWL6HN+lSUtLYWu/nm8eKpHj3S7mIJq0lhxHPz";

	public static final Sha256Hash FEE_TRANSACTION = new Sha256Hash(
			"2b929f20cc6042612d431164063f31ec4c8c42ab7c7d8e92e23a1eb6e882d641");

	public static final Sha256Hash CORRECT_TRANSACTION_HASH = new Sha256Hash(
			"7820a8fd8a290763b6673053e5233b2333a183df9aec3e7716b62df44922e0d6");

	public static DiannaBlock buildCorrectBlock() {
		TestBlockBuilder builder = new TestBlockBuilder();
		builder.withTimestamp(new DateTime(0));
		builder.withNamespace(0);
		builder.withPrice(BigDecimal.TEN);
		builder.withPerviousBlockHash(PREVIOUS_HASH);
		builder.withParentBlockHash(PARENT_HASH);
		builder.withMerkleRootHash(MERKLE_ROOT_HASH);

		return builder.build();
	}

	public static DomainTransaction buildCorrectTransaction() {

		DomainTransaction t = new DomainTransaction();
		t.setDomain("google.ru");
		t.setValue("192.168.1.1");
		t.setFeeTransaction(FEE_TRANSACTION);
		t.setPrevTransaction(PREVIOUS_HASH);

		byte[] pubkey = Utils.parseAsHexOrBase58(PUB_KEY);
		ECKey key = new ECKey(null, pubkey);
		t.setNextPubkey(key);
		t.setSignature(SIGNATURE);

		return t;
	}
}
