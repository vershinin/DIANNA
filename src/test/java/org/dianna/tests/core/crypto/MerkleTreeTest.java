package org.dianna.tests.core.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.dianna.core.crypto.MerkleTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.Sha256Hash;

@RunWith(MockitoJUnitRunner.class)
public class MerkleTreeTest {

	private static final String KNOWN_MERKLE_ROOT = "6a7109a7fb78b2914ea7f3c1db40cea169ad375383ee3f9d6bb853dd2813de2f";

	@Test
	public void shouldUpdateRoot() {
		MerkleTree tree = new MerkleTree();
		tree.addLeaf(Sha256Hash.createDouble("test".getBytes()));
		Sha256Hash firstMerkleRoot = tree.getRoot();

		tree.addLeaf(Sha256Hash.createDouble("test3".getBytes()));
		Sha256Hash secondMerkleRoot = tree.getRoot();
		assertNotEquals(firstMerkleRoot, secondMerkleRoot);
	}

	@Test
	public void shouldGiveKnownMerkleRoot() {
		MerkleTree tree = new MerkleTree();
		tree.addLeaf(Sha256Hash.createDouble("dianna".getBytes()));
		tree.addLeaf(Sha256Hash.createDouble("bitcoin".getBytes()));
		Sha256Hash root = tree.getRoot();
		assertEquals(new Sha256Hash(KNOWN_MERKLE_ROOT), root);
	}

	@Test
	public void shouldGiveCorrectPath() {
		MerkleTree tree = new MerkleTree();
		for (int i = 0; i < 64; i++) {
			tree.addLeaf(Sha256Hash.create(("test" + i).getBytes()));
		}

		Sha256Hash leaf = Sha256Hash.createDouble("test".getBytes());
		System.out.println("Looking for " + leaf);
		tree.addLeaf(leaf);

		List<Pair<Sha256Hash, Sha256Hash>> path = tree.getPath(leaf);
		for (Pair<Sha256Hash, Sha256Hash> pair : path) {
			System.out.println(pair.getLeft() + ":" + pair.getRight());
		}
		System.out.println("root:" + tree.getRoot());
		// assertNotEquals(firstMerkleRoot, secondMerkleRoot);
	}
	@Test
	public void shouldValidateGivenPath() {
		//given
		MerkleTree tree = new MerkleTree();
		for (int i = 0; i < 64; i++) {
			tree.addLeaf(Sha256Hash.create(("test" + i).getBytes()));
		}

		Sha256Hash leaf = Sha256Hash.createDouble("test".getBytes());
		tree.addLeaf(leaf);
		
		List<Pair<Sha256Hash, Sha256Hash>> path = tree.getPath(leaf);
		
		//then
		assertTrue("Path should be valid",MerkleTree.verifyPath(leaf, tree.getRoot(), path));
	}
}
