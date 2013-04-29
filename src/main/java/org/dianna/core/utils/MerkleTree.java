package org.dianna.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.bitcoin.core.Sha256Hash;
import com.google.common.collect.Maps;


/**
 * 
 * @author ivan
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
		 * Pointer to right child. Leaves have null children.
		 */
		Node right;

		void updateHash() {
			hash = HashUtil.createDoubleHash(left.hash, right.hash);
		}

		@Override
		public String toString() {
			return hash.toString();
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
	 * Build merkle tree using given hashes
	 * 
	 * @param hashes
	 */
	public void buildTree(List<Sha256Hash> hashes) {
		if(CollectionUtils.isEmpty(hashes)){
			throw new IllegalStateException("Cannot build merkle tree for empty hashes list");
		}
		List<Node> leaves = new ArrayList<MerkleTree.Node>(hashes.size());
		for (Sha256Hash sha256Hash : hashes) {
			Node n = new Node();
			n.hash = sha256Hash;
			leaves.add(n);
		}

		List<Node> parents = leaves;

		while (parents.size() != 1) {
			updateNodeMap(parents);
			parents = createParents(parents);
			depth++;
		}

		root = parents.get(0);

	}

	private void updateNodeMap(List<Node> parents) {
		for (Node node : parents) {
			leafToNode.put(node.hash, node);
		}
	}

	private List<Node> createParents(List<Node> children) {
		int childrenCount = children.size();

		// If children count is not even, we should add dublicating nodes
		if (childrenCount % 2 != 0) {
			Node n = new Node();
			n.hash = children.get(childrenCount - 1).hash;
			children.add(n);
		}

		List<Node> parents = new ArrayList<MerkleTree.Node>(childrenCount / 2);
		for (int i = 0; i < childrenCount; i += 2) {
			Node parent = new Node();

			parent.left = children.get(i);
			parent.right = children.get(i + 1);
			parent.left.parent = parent;
			parent.right.parent = parent;

			parent.updateHash();
			parents.add(parent);
		}
		return parents;
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