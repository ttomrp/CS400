
/**
 * @author Tom Perez
 * tperez7@wisc.edu
 * COMPSCI 400 010
 */

import java.util.ArrayList;
import java.util.List;

// DO IMPLEMENT A RED BLACK TREE IN THIS CLASS

/**
 * Defines the operations required of student's RBT class.
 *
 * NOTE: There are many methods in this interface that are required solely to
 * support gray-box testing of the internal tree structure. They must be
 * implemented as described to pass all grading tests.
 * 
 * @author Tomas Perez (tperez@cs.wisc.edu)
 * @param <K> A Comparable type to be used as a key to an associated value.
 * @param <V> A value associated with the given key.
 */
public class BALST<K extends Comparable<K>, V> implements BALSTADT<K, V> {
	public static final boolean red = false;
	public static final boolean black = true;
	public Node<K, V> root;
	public Node<K, V> emptyNode;
	public int N; // number of nodes

	public BALST() {
		root = null;
		N = 0;
	}

	@SuppressWarnings("hiding")
	private class Node<K, V> extends BSTNode<K, V> {

		private Node(K key, V value, BSTNode<K, V> leftChild, BSTNode<K, V> rightChild) {
			super(key, value, leftChild, rightChild);
			// TODO Auto-generated constructor stub
			// red = 0
			// black = 1
		}

		private Node(K key, V value) {
			this(key, value, null, null);
		}

	}

	/**
	 * Returns the key that is in the root node of this ST. If root is null, returns
	 * null.
	 * 
	 * @return key found at root node, or null
	 */
	@Override
	public K getKeyAtRoot() {
		if (root == null) {
			return null;
		}
		return root.key;
	}

	/**
	 * Returns the color of the node at given key. Throws exception if key is null
	 * or if key does not exist in the tree.
	 * 
	 * @param key
	 * @return color of node at given key
	 * @throws IllegalNullKeyException
	 * @throws KeyNotFoundException
	 */
	public boolean getColorOf(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		Node<K, V> searchNode = searchTree(root, key);
		if (searchNode == null) {
			throw new KeyNotFoundException();
		}
		return searchNode.color;
	}

	/**
	 * Tries to find a node with a key that matches the specified key. If a matching
	 * node is found, it returns the returns the key that is in the left child. If
	 * the left child of the found node is null, returns null.
	 * 
	 * @param key A key to search for
	 * @return The key that is in the left child of the found key
	 * 
	 * @throws IllegalNullKeyException if key argument is null
	 * @throws KeyNotFoundException    if key is not found in this RBT
	 */
	@Override
	public K getKeyOfLeftChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		Node<K, V> searchNode = searchTree(root, key);
		if (searchNode == null) {
			throw new KeyNotFoundException();
		} else if (searchNode.left == null) {
			return null;
		} else {
			return searchNode.left.key;
		}
	}

	/**
	 * Tries to find a node with a key that matches the specified key. If a matching
	 * node is found, it returns the returns the key that is in the right child. If
	 * the right child of the found node is null, returns null.
	 * 
	 * @param key A key to search for
	 * @return The key that is in the right child of the found key
	 * 
	 * @throws IllegalNullKeyException if key is null
	 * @throws KeyNotFoundException    if key is not found in this RBT
	 */
	@Override
	public K getKeyOfRightChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		Node<K, V> searchNode = searchTree(root, key);
		if (searchNode == null) {
			throw new KeyNotFoundException();
		} else if (searchNode.right == null) {
			return null;
		} else {
			return searchNode.right.key;
		}
	}

	/**
	 * Returns the height of this RBT. H is defined as the number of levels in the
	 * tree.
	 * 
	 * If root is null, return 0 If root is a leaf, return 1 Else return 1 + max(
	 * height(root.left), height(root.right) )
	 * 
	 * Examples: A RBT with no keys, has a height of zero (0). A RBT with one key,
	 * has a height of one (1). A RBT with two keys, has a height of two (2). A RBT
	 * with three keys, can be balanced with a height of two(2) or it may be linear
	 * with a height of three (3) ... and so on for tree with other heights
	 * 
	 * @return the number of levels that contain keys in this BINARY SEARCH TREE
	 */
	@Override
	public int getHeight() {
		if (N == 1) {
			return 1;
		} else {
			return height(root);
		}
	}

	/**
	 * Helper function for getHeight.
	 * 
	 * @param currentNode
	 * @return height of tree
	 */
	private int height(Node<K, V> currentNode) {
		if (currentNode == null) {
			return 0;
		} else {
			int LH = height((Node<K, V>) currentNode.left);
			int RH = height((Node<K, V>) currentNode.right);
			return 1 + Math.max(LH, RH);
		}
	}

	/**
	 * Returns the keys of the data structure in sorted order. In the case of binary
	 * search trees, the visit order is: L V R
	 * 
	 * If the SearchTree is empty, an empty list is returned.
	 * 
	 * @return List of Keys in-order
	 */
	@Override
	public List<K> getInOrderTraversal() {
		List<K> list = new ArrayList<K>();
		order(root, list, "InOrder");
		return list;
	}

	/**
	 * Returns the keys of the data structure in pre-order traversal order. In the
	 * case of binary search trees, the order is: V L R
	 * 
	 * If the SearchTree is empty, an empty list is returned.
	 * 
	 * @return List of Keys in pre-order
	 */
	@Override
	public List<K> getPreOrderTraversal() {
		List<K> list = new ArrayList<K>();
		order(root, list, "PreOrder");
		return list;
	}

	/**
	 * Returns the keys of the data structure in post-order traversal order. In the
	 * case of binary search trees, the order is: L R V
	 * 
	 * If the SearchTree is empty, an empty list is returned.
	 * 
	 * @return List of Keys in post-order
	 */
	@Override
	public List<K> getPostOrderTraversal() {
		List<K> list = new ArrayList<K>();
		order(root, list, "PostOrder");
		return list;
	}

	/**
	 * Helper function for order traversal methods. Adds key values of nodes to a
	 * list based on traversal type.
	 * 
	 * @param currentNode
	 * @param list
	 * @param travType
	 */
	private void order(Node<K, V> currentNode, List<K> list, String travType) {
		if (currentNode != null) {
			if (travType == "InOrder") {
				order((Node<K, V>) currentNode.left, list, travType);
				list.add(currentNode.key);
				order((Node<K, V>) currentNode.right, list, travType);
			}
			if (travType == "PreOrder") {
				list.add(currentNode.key);
				order((Node<K, V>) currentNode.left, list, travType);
				order((Node<K, V>) currentNode.right, list, travType);
			} else if (travType == "PostOrder") {
				order((Node<K, V>) currentNode.left, list, travType);
				order((Node<K, V>) currentNode.right, list, travType);
				list.add(currentNode.key);
			}
		}
	}

	/**
	 * Returns the keys of the data structure in level-order traversal order.
	 * 
	 * The root is first in the list, then the keys found in the next level down,
	 * and so on.
	 * 
	 * If the SearchTree is empty, an empty list is returned.
	 * 
	 * @return List of Keys in level-order
	 */
	@Override
	public List<K> getLevelOrderTraversal() {
		int level = this.getHeight();
		List<K> list = new ArrayList<K>();
		for (int i = 1; i < level + 1; i++) {
			levelOrder(root, list, i);
		}
		return list;
	}

	/**
	 * Helper function for getLevelOrderTraversal.
	 * 
	 * @param currentNode
	 * @param list
	 * @param level
	 */
	private void levelOrder(Node<K, V> currentNode, List<K> list, int level) {
		if (currentNode == null) {
			return;
		} else if (level == 1) {
			list.add(currentNode.key);
		} else if (level > 1) {
			levelOrder((Node<K, V>) currentNode.left, list, level - 1);
			levelOrder((Node<K, V>) currentNode.right, list, level - 1);
		}
	}

	/**
	 * Add the key,value pair to the data structure and increase the number of keys.
	 * If key is null, throw IllegalNullKeyException; If key is already in data
	 * structure, throw DuplicateKeyException(); Do not increase the num of keys in
	 * the structure, if key,value pair is not added.
	 */
	@Override
	public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
		Node<K, V> tempNode;
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		Node<K, V> searchNode = searchTree(root, key);
		if (searchNode != null) {
			throw new DuplicateKeyException();
		}
		if (root == null) {
			root = new Node<K, V>(key, value);
			root.color = black;
			N++;
		} else {
			insertHelper(root, key, value);
			Node<K, V> insert = searchTree(root, key);
			if (getHeight() > 2) {
				rebalanceInsert(insert);
				tempNode = (Node<K, V>) insert.parent;
				if (tempNode == null || tempNode == root) {
					return;
				} else {
					while (tempNode.key.compareTo(root.key) != 0) {
						rebalanceInsert(tempNode);
						tempNode = (Node<K, V>) tempNode.parent;
					}
				}
			}
			root.color = black;
		}
	}

	/**
	 * Helper function for insert. Recurses down BST to find spot to insert node.
	 * Returns the node passed in. When called it will ultimately return the parent
	 * of the inserted node to the insert public method. An unintended consequence
	 * that I couldn't resolve in time.
	 * 
	 * @param currentNode
	 * @param key
	 * @param value
	 * @return
	 */
	private Node<K, V> insertHelper(Node<K, V> currentNode, K key, V value) {
		if (currentNode == null) {
			N++;
			return new Node<K, V>(key, value);
		} else if (currentNode.key.compareTo(key) > 0) {
			currentNode.left = insertHelper((Node<K, V>) currentNode.left, key, value);
			currentNode.left.parent = currentNode;
		} else {
			currentNode.right = insertHelper((Node<K, V>) currentNode.right, key, value);
			currentNode.right.parent = currentNode;
		}
		return currentNode;
	}

	/**
	 * Helper function for insert to correct correct for red property violations
	 * caused by an insert. Performs all rotations and recoloring needed at the
	 * inserted node that causes the red property violation.
	 * 
	 * @param currentNode
	 */
	private void rebalanceInsert(Node<K, V> currentNode) {
		boolean redViolation = false;
		boolean leftFirst = false;
		boolean rightFirst = false;
		Node<K, V> parentNode = (Node<K, V>) currentNode.parent;
		if (parentNode == null) {
			return;
		}
		if (parentNode.key.compareTo(currentNode.key) > 0) {
			parentNode.left = currentNode;
			parentNode.left.parent = parentNode;
			if (parentNode.color == red && parentNode.left.color == red) {
				redViolation = true;
				rightFirst = true;
			}
		} else {
			parentNode.right = currentNode;
			parentNode.right.parent = parentNode;
			if (parentNode.color == red && parentNode.right.color == red) {
				redViolation = true;
				leftFirst = true;
			}
		}
		// currentNode = parent, currentNode.parent = grandparent
		if (redViolation) {
			while (parentNode.color == red) {
				// currentNode.parent = getParent(root, currentNode.key);
				parentNode.sibling = getSibling(parentNode);
				if (parentNode.sibling == null || parentNode.sibling.color == black) {
					if (rightFirst && parentNode.parent.left == parentNode) {
						this.rightRotate((Node<K, V>) parentNode.parent);
						parentNode.color = black;
						parentNode.right.color = red;
					} else if (rightFirst && parentNode.parent.right == parentNode) {
						this.rightRotate(parentNode);
						this.leftRotate((Node<K, V>) parentNode.parent.parent);
						parentNode.color = black;
						parentNode.parent.color = red;
					} else if (leftFirst && parentNode.parent.right == parentNode) {
						this.leftRotate((Node<K, V>) parentNode.parent);
						parentNode.color = black;
						parentNode.left.color = red;
					} else if (leftFirst && parentNode.parent.left == parentNode) {
						this.leftRotate(parentNode);
						this.rightRotate((Node<K, V>) parentNode.parent.parent);
						parentNode.color = black;
						parentNode.parent.color = red;
					}
				} else {
					parentNode.color = !parentNode.color;
					parentNode.sibling.color = !parentNode.sibling.color;
					parentNode.parent.color = !parentNode.parent.color;
				}
			}
		}
	}

	/**
	 * If key is found, remove the key,value pair from the data structure and
	 * decrease num keys, and return true. If key is not found, do not decrease the
	 * number of keys in the data structure, return false. If key is null, throw
	 * IllegalNullKeyException
	 */
	@Override
	public boolean remove(K key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		if (N == 1 && root.key.compareTo(key) == 0) {
			root = null;
			N--;
			return true;
		}
		Node<K, V> searchNode = searchTree(root, key);
		if (searchNode == null) {
			return false;
		} else {
			removeNode(searchNode);
			N--;
			return true;
		}
	}

	/**
	 * Private helper function for remove method.
	 * 
	 * @param searchNode
	 */
	private void removeNode(Node<K, V> searchNode) {
		Node<K, V> tempNode1;
		Node<K, V> tempNode2;
		boolean tempNode1color;
		tempNode1 = searchNode;
		tempNode1color = tempNode1.color;
		if (searchNode.left == null) {
			tempNode2 = (Node<K, V>) searchNode.right;
			parentkidReplacement(searchNode, (Node<K, V>) searchNode.right);
		} else if (searchNode.right == null) {
			tempNode2 = (Node<K, V>) searchNode.left;
			parentkidReplacement(searchNode, (Node<K, V>) searchNode.left);
		} else {
			tempNode1 = minimum((Node<K, V>) searchNode.right);
			tempNode1color = tempNode1.color;
			tempNode2 = (Node<K, V>) tempNode1.right;
			if (tempNode1.parent == searchNode) {
				tempNode2.parent = tempNode1;
			} else {
				parentkidReplacement(tempNode1, (Node<K, V>) tempNode1.right);
				tempNode1.right = searchNode.right;
				tempNode1.right.parent = tempNode1;
			}
			parentkidReplacement(searchNode, tempNode1);
			tempNode1.left = searchNode.left;
			tempNode1.left.parent = tempNode1;
			tempNode1.color = searchNode.color;
		}
		if (tempNode1color == black) {
			rebalanceRemove(tempNode2);
		}
	}

	/**
	 * Helper function for remove to rebalance the tree after removal of a node
	 * 
	 * @param currentNode
	 */
	private void rebalanceRemove(Node<K, V> currentNode) {
		Node<K, V> tempNode;
		while (currentNode != root && currentNode.color == black) {
			if (currentNode.parent.left == currentNode) {
				tempNode = (Node<K, V>) currentNode.parent.right;
				if (tempNode.color == red) {
					tempNode.color = black;
					currentNode.parent.color = red;
					leftRotate((Node<K, V>) currentNode.parent);
					tempNode = (Node<K, V>) currentNode.parent.right;
				}
				if (tempNode.left.color == black && tempNode.right.color == black) {
					tempNode.color = red;
					currentNode = (Node<K, V>) currentNode.parent;
				} else {
					if (tempNode.right.color == black) {
						tempNode.left.color = black;
						tempNode.color = red;
						rightRotate(tempNode);
						tempNode = (Node<K, V>) currentNode.parent.right;
					}
					tempNode.color = currentNode.parent.color;
					currentNode.parent.color = black;
					tempNode.right.color = black;
					leftRotate((Node<K, V>) currentNode.parent);
					currentNode = root;
				}
			} else {
				tempNode = (Node<K, V>) currentNode.parent.left;
				if (tempNode.color == red) {
					tempNode.color = black;
					currentNode.parent.color = red;
					rightRotate((Node<K, V>) currentNode.parent);
					tempNode = (Node<K, V>) currentNode.parent.left;
				}
				if (tempNode.right.color == black && tempNode.left.color == black) {
					tempNode.color = red;
					currentNode = (Node<K, V>) currentNode.parent;
				} else {
					if (tempNode.left.color == black) {
						tempNode.right.color = black;
						tempNode.color = red;
						leftRotate(tempNode);
						tempNode = (Node<K, V>) currentNode.parent.left;
					}
					tempNode.color = currentNode.parent.color;
					currentNode.parent.color = black;
					tempNode.left.color = black;
					rightRotate((Node<K, V>) currentNode.parent);
					currentNode = root;
				}
			}
		}
		currentNode.color = black;
	}

	/**
	 * Replaces the parent node with the kid for removal
	 * 
	 * @param p
	 * @param k
	 */
	private void parentkidReplacement(Node<K, V> p, Node<K, V> k) {
		if (k == null) {
			p = null;
		} else {
			if (p.parent == null) {
				root = k;
			} else if (p.parent.left == p) {
				p.parent.left = k;
			} else {
				p.parent.right = k;
			}
			p.parent = k.parent;
		}
	}

	/**
	 * Returns the value associated with the specified key.
	 *
	 * Does not remove key or decrease number of keys If key is null, throw
	 * IllegalNullKeyException If key is not found, throw KeyNotFoundException().
	 */
	@Override
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		Node<K, V> searchNode = searchTree(root, key);
		if (searchNode == null) {
			throw new KeyNotFoundException();
		} else
			return searchNode.value;
	}

	/**
	 * Returns true if the key is in the data structure If key is null, throw
	 * IllegalNullKeyException Returns false if key is not null and is not present
	 */
	@Override
	public boolean contains(K key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		Node<K, V> searchNode = searchTree(root, key);
		if (searchNode == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Generic search helper for multiple methods. From a given start node, it
	 * recurses down the tree searching for a node with the given key. Throws
	 * IllegalNullKey Exception if key is null. Returns null if node is not found
	 * with given key. Returns node that has given key.
	 * 
	 * @param currentNode = starting node
	 * @param key
	 * @return node with given key
	 * @throws IllegalNullKeyException
	 */
	private Node<K, V> searchTree(Node<K, V> currentNode, K key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		if (currentNode == null) {
			return null;
		}
		if (currentNode.key.compareTo(key) == 0) {
			return currentNode;
		} else if (currentNode.key.compareTo(key) > 0) {
			return searchTree((Node<K, V>) currentNode.left, key);
		} else {
			return searchTree((Node<K, V>) currentNode.right, key);
		}
	}

	/**
	 * Method to find the parent node of the node with the searched key. Returns the
	 * parent node of the node with the given key.
	 * 
	 * @param currentNode
	 * @param key
	 * @return parent node
	 */
	private Node<K, V> getParent(Node<K, V> currentNode, K key) {
		if (currentNode == null) {
			return null;
		}
		if (currentNode.left != null) {
			if (currentNode.left.key.compareTo(key) == 0) {
				return currentNode;
			}
		}
		if (currentNode.right != null) {
			if (currentNode.right.key.compareTo(key) == 0) {
				return currentNode;
			}
		}
		if (currentNode.key.compareTo(key) > 0) {
			return getParent((Node<K, V>) currentNode.left, key);
		} else {
			return getParent((Node<K, V>) currentNode.right, key);
		}
	}

	/**
	 * Method to find the sibling of a node if one exists. If not, returns null.
	 * Otherwise, returns the sibling node to given node.
	 * 
	 * @param currentNode
	 * @return
	 */
	private Node<K, V> getSibling(Node<K, V> currentNode) {
		currentNode.parent = getParent(root, currentNode.key);
		if (currentNode.parent.left == currentNode && currentNode.parent.right != null) {
			return (Node<K, V>) currentNode.parent.right;
		} else if (currentNode.parent.right == currentNode && currentNode.parent.left != null) {
			return (Node<K, V>) currentNode.parent.left;
		} else {
			return null;
		}
	}

	/**
	 * Returns the number of key,value pairs in the data structure
	 */
	@Override
	public int numKeys() {
		return N;
	}

	/**
	 * Searches for the smallest (furthest left) node in a tree.
	 * 
	 * @param currentNode
	 * @return
	 */
	private Node<K, V> minimum(Node<K, V> currentNode) {
		while (currentNode.left != null) {
			currentNode = (Node<K, V>) currentNode.left;
		}
		return currentNode;
	}

	/**
	 * Method to perform the left rotation of a given node. The parent node is
	 * passed in and the left rotation is performed at the parent node.
	 * 
	 * @param p
	 */
	private void leftRotate(Node<K, V> p) {
		// Node<K, V> g = getParent(this.root, p.key);
		Node<K, V> k = (Node<K, V>) p.right;
		p.right = k.left;
		if (k.left != null) {
			k.left.parent = p;
		}
		k.parent = p.parent;
		if (p.parent == null) {
			root = k;
		} else if (p.parent.left == p) {
			p.parent.left = k;
		} else {
			p.parent.right = k;
		}
		k.left = p;
		p.parent = k;
	}

	/**
	 * Method to perform the right rotation of a given node. The parent node is
	 * passed in and the right rotation is performed at the parent node.
	 * 
	 * @param p
	 */
	private void rightRotate(Node<K, V> p) {
		// Node<K, V> g = getParent(this.root, p.key);
		Node<K, V> k = (Node<K, V>) p.left;
		p.left = k.right;
		if (k.right != null) {
			k.right.parent = p;
		}
		k.parent = p.parent;
		if (p.parent == null) {
			root = k;
		} else if (p.parent.right == p) {
			p.parent.right = k;
		} else {
			p.parent.left = k;
		}
		k.right = p;
		p.parent = k;
	}

	/**
	 * Print the tree.
	 *
	 * For our testing purposes of your print method: all keys that we insert in the
	 * tree will have a string length of exactly 2 characters. example: numbers
	 * 10-99, or strings aa - zz, or AA to ZZ
	 *
	 * This makes it easier for you to not worry about spacing issues.
	 *
	 * You can display a binary search in any of a variety of ways, but we must see
	 * a tree that we can identify left and right children of each node
	 *
	 * For example:
	 * 
	 * 30 /\ / \ 20 40 / /\ / / \ 10 35 50
	 * 
	 * Look from bottom to top. Inorder traversal of above tree (10,20,30,35,40,50)
	 * 
	 * Or, you can display a tree of this kind.
	 * 
	 * | |-------50 |-------40 | |-------35 30 |-------20 | |-------10
	 * 
	 * Or, you can come up with your own orientation pattern, like this.
	 * 
	 * 10 20 30 35 40 50
	 * 
	 * The connecting lines are not required if we can interpret your tree.
	 * 
	 */
	@Override
	public void print() {
		int level = getHeight();
		for (int i = 1; i < level + 1; i++) {
			printLevel(root, i);
			System.out.println();
		}
	}

	/**
	 * Private helper method for the print method.
	 * 
	 * @param currentNode
	 * @param level
	 */
	private void printLevel(Node<K, V> currentNode, int level) {
		String color;
		if (currentNode == null) {
			return;
		} else if (level == 1) {
			if (currentNode.color == black) {
				color = "Black";
			} else {
				color = "Red";
			}
			System.out.print(currentNode.key + "(" + color + ")" + " ");
		} else if (level > 1) {
			printLevel((Node<K, V>) currentNode.left, level - 1);
			printLevel((Node<K, V>) currentNode.right, level - 1);
		}
	}

} // copyrighted material, students do not have permission to post on public sites

//  deppeler@cs.wisc.edu
