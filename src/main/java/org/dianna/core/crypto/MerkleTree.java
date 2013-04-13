package org.dianna.core.crypto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.bitcoin.core.Sha256Hash;
import com.google.common.collect.Maps;

/**
 * Implmentation of a binary Merkle tree commitment using a generic
 * collision-resistant hash.
 * 
 * @author talm
 * 
 */
public class MerkleTree {

	/**
	 * The root of the Merkle Tree.
	 */
	protected Node root;

	/**
	 * The maximum length of a path from a leaf to the root.
	 */
	protected int depth;

	/**
	 * A map from leaf hashvalues to the nodes that represent them.
	 */
	protected Map<Sha256Hash, Node> leafToNode;

	/**
	 * A node in the Merkle tree. Each node has a value and pointers to its
	 * child nodes. The value is computed by hashing the values of the child
	 * nodes.
	 * 
	 * @author talm
	 * 
	 */
	protected class Node {
		/**
		 * Value of the node. This value is a commitment to the values of its
		 * children.
		 */
		Sha256Hash hash;

		/**
		 * Parent of this node in the tree. The root has a null parent.
		 */
		Node parent;

		/**
		 * Pointer to left child. Leaves have null children.
		 */
		Node left;

		/**
		 * Number of leaves under the left child.
		 */
		int leftSubtreeSize;

		/**
		 * Pointer to right child. Leaves have null children.
		 */
		Node right;

		/**
		 * Number of leaves under the right child.
		 */
		int rightSubtreeSize;

		void updateHash() {
			Sha256Hash leftHash = left.hash;

			Sha256Hash rightHash = null;
			if (right != null) {
				rightHash = right.hash;
			} else {
				rightHash = left.hash;
			}

			hash = Sha256Hash.createDouble(ArrayUtils.addAll(leftHash.getBytes(), rightHash.getBytes()));
		}

	}

	public MerkleTree() {
		root = new Node();
		depth = 0;
		leafToNode = Maps.newHashMap();
	}

	public Sha256Hash getRoot() {
		return root.hash;
	}

	/**
	 * Add a leaf to the tree.
	 * 
	 * @param leafvalue
	 * @return true if the leaf was added, false if it was already in the tree.
	 */
	public boolean addLeaf(Sha256Hash leafvalue) {
		if (leafToNode.containsKey(leafvalue))
			return false;

		Node curNode = root;

		Node leafNode = new Node();
		leafNode.hash = leafvalue;
		leafToNode.put(leafvalue, leafNode);

		if (root.left == null) {
			assert root.right == null;
			root.left = leafNode;
			leafNode.parent = root;
			root.leftSubtreeSize = 1;
			root.updateHash();
			depth = 1;
		} else if (root.right == null) {
			root.right = leafNode;
			leafNode.parent = root;
			root.rightSubtreeSize = 1;
			root.updateHash();
		} else {
			/*
			 * Repeatedly choose the child with the smallest subtree until we
			 * reach a leaf.
			 */
			int curdepth = 0;
			while (curNode.leftSubtreeSize > 0) {
				if (curNode.leftSubtreeSize > curNode.rightSubtreeSize)
					curNode = curNode.right;
				else
					curNode = curNode.left;
				++curdepth;
			}

			assert curdepth <= depth;

			// We've reached a leaf.
			assert curNode.rightSubtreeSize == 0;

			// Create a new internal node, and insert it in place of curNode.
			// curNode will become the new left node, and the new leaf will
			// become
			// its new right node.
			Node newInternal = new Node();
			newInternal.leftSubtreeSize = newInternal.rightSubtreeSize = 1;
			newInternal.parent = curNode.parent;
			if (curNode.parent.left == curNode) {
				curNode.parent.left = newInternal;
			} else {
				assert curNode.parent.right == curNode;
				curNode.parent.right = newInternal;
			}
			newInternal.left = curNode;
			newInternal.right = leafNode;
			leafNode.parent = curNode.parent = newInternal;
			newInternal.updateHash();

			if (curdepth == depth)
				++depth;

			// Update the subtree counts and hashes on the path to the root.
			for (curNode = newInternal.parent; curNode != null; curNode = curNode.parent) {
				curNode.leftSubtreeSize = curNode.left.leftSubtreeSize + curNode.left.rightSubtreeSize;
				curNode.rightSubtreeSize = curNode.right.leftSubtreeSize + curNode.right.rightSubtreeSize;
				curNode.updateHash();
			}
		}

		return true;
	}

	public List<Pair<Sha256Hash, Sha256Hash>> getPath(Sha256Hash leaf) {
		Node curNode = leafToNode.get(leaf);
		if (curNode == null)
			return null;

		List<Pair<Sha256Hash, Sha256Hash>> path = new ArrayList<Pair<Sha256Hash, Sha256Hash>>(depth);
		for (curNode = curNode.parent; curNode != null; curNode = curNode.parent) {
			// The tree is left-leaning -- since there exists at least one node,
			// the left child of every non-leaf node must exist.
			assert curNode.left != null;
			path.add(Pair.of(curNode.left.hash, curNode.right != null ? curNode.right.hash : curNode.left.hash));
		}

		return path;
	}

	public static boolean verifyPath(Sha256Hash leaf, Sha256Hash root, List<Pair<Sha256Hash, Sha256Hash>> path) {
		// Verify that the each pair contains a commitment
		// to the previous pair and that the first pair
		// in the path contains the leaf
		Sha256Hash lasthash = leaf;
		for (Pair<Sha256Hash, Sha256Hash> pair : path) {
			if (!lasthash.equals(pair.getLeft()) && !lasthash.equals(pair.getRight()))
				return false;
			lasthash = HashUtil.createDoubleHash(pair.getLeft(), pair.getRight());
		}
		// Verify that the path ends in root.
		if (!lasthash.equals(root))
			return false;

		// All the tests passed.
		return true;
	}

}