import java.util.LinkedList;

// TODO: comment and complete your HashTableADT implementation
//
// TODO: implement all required methods
// DO ADD REQUIRED PUBLIC METHODS TO IMPLEMENT interfaces
//
// DO NOT ADD ADDITIONAL PUBLIC MEMBERS TO YOUR CLASS 
// (no public or package methods that are not in implemented interfaces)
//
// TODO: describe the collision resolution scheme you have chosen
// identify your scheme as open addressing or bucket
//
// if open addressing: describe probe sequence 
// if buckets: describe data structure for each bucket
//
// TODO: explain your hashing algorithm here 

/**
 * HashTable implementation that uses:
 * 
 * @param <K> unique comparable identifier for each <K,V> pair, may not be null
 * @param <V> associated value with a key, value may be null
 */
public class BookHashTable implements HashTableADT<String, Book> {

	/** The initial capacity that is used if none is specified user */
	static final int DEFAULT_CAPACITY = 101;

	/** The load factor that is used if none is specified by user */
	static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;

	int tableSize;
	double loadFactorThreshold;
	LinkedList<Node<String, Book>>[] hashtable;
	int numKeys;

	@SuppressWarnings("hiding")
	private class Node<String, Book> {
		private String key;
		private Book value;

		private Node(String key, Book value) {
			this.key = key;
			this.value = value;
		}

		private String getKey() {
			return key;
		}

		private Book getBook() {
			return value;
		}
	}

	/**
	 * REQUIRED default no-arg constructor Uses default capacity and sets load
	 * factor threshold for the newly created hash table.
	 */
	public BookHashTable() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR_THRESHOLD);
	}

	/**
	 * Creates an empty hash table with the specified capacity and load factor.
	 * 
	 * @param initialCapacity     number of elements table should hold at start.
	 * @param loadFactorThreshold the ratio of items/capacity that causes table to
	 *                            resize and rehash
	 */
	@SuppressWarnings("unchecked")
	public BookHashTable(int initialCapacity, double loadFactorThreshold) {
		// TODO: comment and complete a constructor that accepts initial capacity
		// and load factor threshold and initializes all fields

		tableSize = initialCapacity;
		this.loadFactorThreshold = loadFactorThreshold;
		hashtable = (LinkedList<BookHashTable.Node<String, Book>>[]) new LinkedList<?>[tableSize];
		numKeys = 0;
	}

	/**
	 * Computes the hash index using Java's hasCode() method
	 * 
	 * @param key
	 * @return
	 * @throws IllegalNullKeyException
	 */
	private int hashFunction(String key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		return Math.abs(key.hashCode()) % tableSize;
	}

	/**
	 * Inserts a key-book pair into the hash table. Will check and resize the table
	 * before insertion.
	 * 
	 * @param key
	 * @param value - book
	 * @throws IllegalNullKeyException
	 * @throws DuplicateKeyException
	 */
	@Override
	public void insert(String key, Book value) throws IllegalNullKeyException, DuplicateKeyException {
		// TODO Auto-generated method stub
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		double N = numKeys;
		double TS = tableSize;
		double loadFactor = N / TS;
		if (loadFactor >= loadFactorThreshold) {
			resizeTable();
		}
		int hashIndex = hashFunction(key);
		Node<String, Book> currentNode = new Node<String, Book>(key, value);
		// if hashIndex is empty create new list and add key-value pair
		if (hashtable[hashIndex] == null) {
			hashtable[hashIndex] = new LinkedList<Node<String, Book>>();
			hashtable[hashIndex].add(currentNode);
		} else {
			// check for duplicate key
			for (int i = 0; i < hashtable[hashIndex].size(); i++) {
				if (hashtable[hashIndex].get(i).getKey().compareTo(key) == 0) {
					throw new DuplicateKeyException();
				}
			}
			// if no duplicate key found in list, add key-value pair
			hashtable[hashIndex].add(currentNode);
		}
		numKeys++;
	}

	/**
	 * Resizes the hash table to (2x+1) where x is the original table size.
	 * Reinserts all key-book pairs back into hash table at new indices.
	 * 
	 * @throws IllegalNullKeyException
	 * @throws DuplicateKeyException
	 */
	@SuppressWarnings("unchecked")
	private void resizeTable() throws IllegalNullKeyException, DuplicateKeyException {
		LinkedList<Node<String, Book>>[] currentTable = hashtable;
		int currentSize = tableSize;
		tableSize = (2 * tableSize) + 1;
		hashtable = (LinkedList<BookHashTable.Node<String, Book>>[]) new LinkedList<?>[tableSize];
		numKeys = 0;
		for (int i = 0; i < currentSize; i++) {
			if (currentTable[i] != null) {
				for (int j = 0; j < currentTable[i].size(); j++) {
					insert(currentTable[i].get(j).getKey(), currentTable[i].get(j).getBook());
				}
			}
		}
	}

	/**
	 * Removes key-book pair from hash table and decrements numKeys.
	 * 
	 * @param key
	 * @return true if removal successful, false otherwise
	 */
	@Override
	public boolean remove(String key) throws IllegalNullKeyException {
		// TODO Auto-generated method stub
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		int hashIndex = hashFunction(key);
		if (hashtable[hashIndex] == null) {
			return false;
		} else {
			for (int i = 0; i < hashtable[hashIndex].size(); i++) {
				if (hashtable[hashIndex].get(i).getKey().compareTo(key) == 0) {
					hashtable[hashIndex].remove(i);
					numKeys--;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the value/book for a given key.
	 * 
	 * @param key
	 * @throws IllegalNullKeyException
	 * @throws KeyNotFoundException
	 */
	@Override
	public Book get(String key) throws IllegalNullKeyException, KeyNotFoundException {
		// TODO Auto-generated method stub
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		int hashIndex = hashFunction(key);
		if (hashtable[hashIndex] == null) {
			throw new KeyNotFoundException();
		} else {
			for (int i = 0; i < hashtable[hashIndex].size(); i++) {
				if (hashtable[hashIndex].get(i).getKey().compareTo(key) == 0) {
					return hashtable[hashIndex].get(i).getBook();
				}
			}
		}
		throw new KeyNotFoundException();
	}

	// Gets the number of keys in the hash table.
	@Override
	public int numKeys() {
		// TODO Auto-generated method stub
		return numKeys;
	}

	// Gets the load factor threshold for a hash table.
	@Override
	public double getLoadFactorThreshold() {
		// TODO Auto-generated method stub
		return loadFactorThreshold;
	}

	// Gets the current size of hash table
	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return tableSize;
	}

	// Returns the chosen collision resolution scheme.
	@Override
	public int getCollisionResolutionScheme() {
		// TODO Auto-generated method stub
		// 1 OPEN ADDRESSING: linear probe
		// 2 OPEN ADDRESSING: quadratic probe
		// 3 OPEN ADDRESSING: double hashing
		// 4 CHAINED BUCKET: array list of array lists
		// 5 CHAINED BUCKET: array list of linked lists
		// 6 CHAINED BUCKET: array list of binary search trees
		// 7 CHAINED BUCKET: linked list of array lists
		// 8 CHAINED BUCKET: linked list of linked lists
		// 9 CHAINED BUCKET: linked list of of binary search trees
		return 5;
	}

	// TODO: add all unimplemented methods so that the class can compile

}
