/**
 * @author Tom Perez
 * tperez7@wisc.edu
 * COMPSCI 400 010
 */



/**
 * Implementation of DataStructureADT
 * @author Tom Perez
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DS_My implements DataStructureADT {
	
	private Node root;
	private int size;
	
    public DS_My() {
        root=null;
        size=0;
    }

	private class Node<K extends Comparable<K>,V> {
		private K key;
		private V value;
		private Node next;
		
		/**
		 * Node Constructor
		 * @param key
		 * @param value
		 * @throws IllegalArgumentException
		 */
		private Node(K key, V value) throws IllegalArgumentException {
			if (key == null)
				throw new IllegalArgumentException("Null Key");
			this.key = key;
			this.value = value;
			this.next = null;
		}
	}

	/**
	 * Inserts key/value pair and increments the size.
	 * @param K is key of new Node
	 * @param V is value of new Node
	 * @throws IllegalArgumentException when K is null
	 * @throws RuntimeException when K already exists
	 */
	@Override
	public void insert(Comparable K, Object V) throws IllegalArgumentException, RuntimeException {
    	if (K == null)
    		throw new IllegalArgumentException("Null Key");
    	if (this.contains(K))
    		throw new RuntimeException("Duplicate Key");
    	if (root == null) {
    		root = new Node(K,V);
    		size++;
    	}
    	else {
    		Node currentNode = root;
    		while (currentNode.next != null)
    		    currentNode = currentNode.next;
    		currentNode.next = new Node(K,V);
    		size++;
    	}
    }
	
	/**
	 * Removes key/value pair and increments size
	 * @param K is key of Node
	 * @throws IllegalArgumentException when K is null
	 * @return true if K removed, false otherwise
	 */
    @Override
	public boolean remove(Comparable K) throws IllegalArgumentException {
        if (K == null)
            throw new IllegalArgumentException("Null Key");
        if (root == null) 
            return false;
        if (this.contains(K) == false)
        	return false;
        if(root.key.equals(K)) {
            root = root.next;
            size--;
            return true;
        }
        Node currentNode = root.next;
        Node lastNode = root;
        while (currentNode != null) {
            lastNode = currentNode;
            currentNode = currentNode.next;
            if (lastNode.key.equals(K)) {
            	lastNode.next = lastNode.next;
            	size --;
            	return true;
            }
        }   
        return false;
    }
    
    /**
     * Checks for existing key in DS
     * @param K is key of Node
     * @return true if K exists, false otherwise
     */
    @Override
	public boolean contains(Comparable K) {
    	if (K == null || root == null)
    		return false;
    	if (root.key.equals(K))
    		return true;
    	Node currentNode = root;
    	while (currentNode.next != null) {
    		currentNode = currentNode.next;
    		if (currentNode.key.equals(K))
    			return true;
    	}
    	return false;
    }
    
    /**
     * Gets the value of Node with key K
     * @param K is key of Node
     * @throws IllegalArgumentException
     * @return value of Node with key K
     */
    @Override
	public Object get(Comparable K) throws IllegalArgumentException{
    	if (K == null)
    		throw new IllegalArgumentException("Null Key");
    	if (root == null)
    		return null;
    	if (root.key.equals(K))
    		return root.value;
    	Node currentNode = root;
    	while (currentNode.key.equals(K)) {
    		currentNode = currentNode.next;
    		if (currentNode.key.equals(K))
    			return currentNode.next;
    	}
    	return null;
    }
    
    /**
     * Gets the size of the DS
     * @return size of DS
     */
    @Override
    public int size() {
    	return size;
    }
    //End of public class DS_My
}                            
    
