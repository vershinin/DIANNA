package org.dianna.tests.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;

import org.dianna.core.crypto.HashUtil;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.tests.builders.TestDataFactory;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;

@RunWith(MockitoJUnitRunner.class)
public class HashUtilTest {

	@Test
	public void shouldGiveCorrectHashForKnownBlock() {
		// given
		DiannaBlock block = TestDataFactory.buildCorrectBlock();

		// when
		Sha256Hash hash = HashUtil.getHash(block);

		// then
		assertEquals(TestDataFactory.CORRECT_BLOCK_HASH, hash);
	}

	@Test
	public void shouldChangeHashIfTimestampChanged() {
		// given
		DiannaBlock block = TestDataFactory.buildCorrectBlock();
		block.setTimestamp(DateTime.now());

		// when
		Sha256Hash hash = HashUtil.getHash(block);

		// then
		assertNotEquals("Hash should change", TestDataFactory.CORRECT_BLOCK_HASH, hash);
	}

	@Test
	public void shouldChangeHashIfNamespaceChanged() {
		// given
		DiannaBlock block = TestDataFactory.buildCorrectBlock();
		block.setNamespace(2);

		// when
		Sha256Hash hash = HashUtil.getHash(block);

		// then
		assertNotEquals("Hash should change", TestDataFactory.CORRECT_BLOCK_HASH, hash);
	}

	@Test
	public void shouldChangeHashIfPriceHasChanged() {
		// given
		DiannaBlock block = TestDataFactory.buildCorrectBlock();
		block.setPrice(BigDecimal.ZERO);

		// when
		Sha256Hash hash = HashUtil.getHash(block);

		// then
		assertNotEquals("Hash should change", TestDataFactory.CORRECT_BLOCK_HASH, hash);
	}

	@Test
	public void shouldChangeHashIfPrevBlockChanged() {
		// given
		DiannaBlock block = TestDataFactory.buildCorrectBlock();
		block.setPrevBlockHash(Sha256Hash.ZERO_HASH);

		// when
		Sha256Hash hash = HashUtil.getHash(block);

		// then
		assertNotEquals("Hash should change", TestDataFactory.CORRECT_BLOCK_HASH, hash);
	}

	@Test
	public void shouldChangeHashIfMerkleRootChanged() {
		// given
		DiannaBlock block = TestDataFactory.buildCorrectBlock();
		block.setMerkleRootHash(Sha256Hash.ZERO_HASH);

		// when
		Sha256Hash hash = HashUtil.getHash(block);

		// then
		assertNotEquals("Hash should change", TestDataFactory.CORRECT_BLOCK_HASH, hash);
	}

	// TRANSACTIONS

	@Test
	public void shouldGiveCorrectHashForKnownTransaction() {
		// given
		DomainTransaction tx = TestDataFactory.buildCorrectTransaction();

		// when
		Sha256Hash hash = HashUtil.getHash(tx);

		// then
		assertEquals(TestDataFactory.CORRECT_TRANSACTION_HASH, hash);
	}

	@Test
	public void shouldChangeHashIfDomainChanged() {
		// given
		DomainTransaction tx = TestDataFactory.buildCorrectTransaction();
		tx.setDomain("other domain");

		// when
		Sha256Hash hash = HashUtil.getHash(tx);

		// then
		assertNotEquals("Hash should change", hash, TestDataFactory.CORRECT_TRANSACTION_HASH);
	}

	@Test
	public void shouldChangeHashIfValueChanged() {
		// given
		DomainTransaction tx = TestDataFactory.buildCorrectTransaction();
		tx.setValue("new value");

		// when
		Sha256Hash hash = HashUtil.getHash(tx);

		// then
		assertNotEquals("Hash should change", hash, TestDataFactory.CORRECT_TRANSACTION_HASH);
	}

	@Test
	public void shouldChangeHashIfPrevTransactionChanged() {
		// given
		DomainTransaction tx = TestDataFactory.buildCorrectTransaction();
		tx.setPrevTransaction(Sha256Hash.ZERO_HASH);

		// when
		Sha256Hash hash = HashUtil.getHash(tx);

		// then
		assertNotEquals("Hash should change", hash, TestDataFactory.CORRECT_TRANSACTION_HASH);
	}

	@Test
	public void shouldChangeHashIfFeeTransactionChanged() {
		// given
		DomainTransaction tx = TestDataFactory.buildCorrectTransaction();
		tx.setFeeTransaction(Sha256Hash.ZERO_HASH);

		// when
		Sha256Hash hash = HashUtil.getHash(tx);

		// then
		assertNotEquals("Hash should change", hash, TestDataFactory.CORRECT_TRANSACTION_HASH);
	}

	@Test
	public void shouldChangeHashIfKeyChanged() {
		// given
		DomainTransaction tx = TestDataFactory.buildCorrectTransaction();
		tx.setNextPubkey(new ECKey());

		// when
		Sha256Hash hash = HashUtil.getHash(tx);

		// then
		assertNotEquals("Hash should change", hash, TestDataFactory.CORRECT_TRANSACTION_HASH);
	}

	@Test
	public void shouldChangeHashIfSignatureChanged() {
		// given
		DomainTransaction tx = TestDataFactory.buildCorrectTransaction();
		tx.setSignature("other fake signature");

		// when
		Sha256Hash hash = HashUtil.getHash(tx);

		// then
		assertNotEquals("Hash should change", hash, TestDataFactory.CORRECT_TRANSACTION_HASH);
	}

	// TEST DATA

}
