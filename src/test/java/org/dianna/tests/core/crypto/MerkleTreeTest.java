package org.dianna.tests.core.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.dianna.core.crypto.MerkleTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.beust.jcommander.internal.Lists;
import com.google.bitcoin.core.Sha256Hash;

@RunWith(MockitoJUnitRunner.class)
public class MerkleTreeTest {

	private static final Sha256Hash ROOT = new Sha256Hash(
			"25207db7f8027354fe997d6282342ce3a3f79896d1748285b43a0203c3493b68");

	Sha256Hash hash1 = new Sha256Hash("1000000000000000000000000000000000000000000000000000000000000000");
	Sha256Hash hash2 = new Sha256Hash("2000000000000000000000000000000000000000000000000000000000000000");
	Sha256Hash hash3 = new Sha256Hash("3000000000000000000000000000000000000000000000000000000000000000");
	Sha256Hash hash4 = new Sha256Hash("4000000000000000000000000000000000000000000000000000000000000000");
	Sha256Hash hash5 = new Sha256Hash("5000000000000000000000000000000000000000000000000000000000000000");
	public List<Sha256Hash> hashList = Lists.newArrayList(hash1, hash2, hash3, hash4, hash5);

	@Test
	public void shouldUpdateRoot() {
		// MerkleTree tree = new MerkleTree();
		// tree.addLeaf(Sha256Hash.createDouble("test".getBytes()));
		// Sha256Hash firstMerkleRoot = tree.getRoot();
		//
		// tree.addLeaf(Sha256Hash.createDouble("test3".getBytes()));
		// Sha256Hash secondMerkleRoot = tree.getRoot();
		// assertNotEquals(firstMerkleRoot, secondMerkleRoot);
	}

	@Test
	public void shouldGiveKnownMerkleRoot() {
		// given
		MerkleTree tree = new MerkleTree();
		tree.buildTree(hashList);

		// when
		Sha256Hash root = tree.getRoot();

		// then
		assertEquals(ROOT, root);
	}

	@Test
	public void shouldGiveCorrectPath() {
		// given
		MerkleTree tree = new MerkleTree();
		tree.buildTree(hashList);

		// when
		List<Pair<Sha256Hash, Sha256Hash>> path = tree.getPath(hash1);

		// then
		Pair<Sha256Hash, Sha256Hash> pair = path.get(0);
		assertTrue(pair.getLeft() == hash1 && pair.getRight() == hash2);

		pair = path.get(1);
		assertEquals(new Sha256Hash("73512760f320a3af88914a637c51395034745ad02a31305a9a198fe975a1a3a1"), pair.getLeft());
		assertEquals(new Sha256Hash("36ce40300ba3b2ed39f30c31e5e008dafd198c81d221053aaa7c3425108be534"),
				pair.getRight());

		pair = path.get(2);
		assertEquals(new Sha256Hash("60fa468d43dedaa9c1022b1bf83cddfece4f83f4afca461e3829efa7e0c0d774"), pair.getLeft());
		assertEquals(new Sha256Hash("d04ff478901282f12a070d15bcee3cd0f1a7cb7ea35cb2a2cbd59a71e354ee18"),
				pair.getRight());

		assertEquals(new Sha256Hash("25207db7f8027354fe997d6282342ce3a3f79896d1748285b43a0203c3493b68"), tree.getRoot());

	}

	@Test
	public void shouldValidateGivenPath() {
		// given
		MerkleTree tree = new MerkleTree();
		tree.buildTree(hashList);

		// when
		List<Pair<Sha256Hash, Sha256Hash>> path = tree.getPath(hash4);

		// then
		assertTrue("Path should be valid", MerkleTree.verifyPath(hash4, tree.getRoot(), path));
	}
}
