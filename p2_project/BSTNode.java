// Students may use and edit this class as they choose
// Students may add or remove or edit fields, methods, constructors for this class
// Students may inherit from an use this class in any way internally in other classes.
// Students are not required to use this class. 
// BUT, IF YOUR CODE USES THIS CLASS, BE SURE TO SUBMIT THIS CLASS
//
// RECOMMENDED: do not use public or private visibility modifiers
// and make this an inner class in your tree implementation.
//
// package level access means that all classes in the package can access directly.
// and accessing the node fields from classes other than your balanced search 
// is bad design as it creates many more chances for bugs to be introduced and not
// caught.
//
// Classes that use this type:  <TODO, list which if any classes use this type>
class BSTNode<K, V> {
	K key;
	V value;
	BSTNode<K, V> left;
	BSTNode<K, V> right;
	BSTNode<K, V> parent; // added
	BSTNode<K, V> sibling; // added
	int balanceFactor;
	int height;
	boolean color; // added 0=red, 1=black

	/**
	 * @param key
	 * @param value
	 * @param leftChild
	 * @param rightChild
	 */
	BSTNode(K key, V value, BSTNode<K, V> leftChild, BSTNode<K, V> rightChild) {
		this.key = key;
		this.value = value;
		this.left = leftChild;
		this.right = rightChild;
		this.height = 0;
		this.balanceFactor = 0;
		this.color = false; // added, false is red
		this.parent = null; // added
		this.sibling = null; // added
	}

	BSTNode(K key, V value) {
		this(key, value, null, null);
	}

}
