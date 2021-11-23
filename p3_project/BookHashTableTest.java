
/**
 * Filename:   TestHashTableDeb.java
 * Project:    p3
 * Authors:    Debra Deppeler (deppeler@cs.wisc.edu)
 * 
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   before 10pm on 10/29
 * Version:    1.0
 * 
 * Credits:    None so far
 * 
 * Bugs:       TODO: add any known bugs, or unsolved problems here
 */

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test HashTable class implementation to ensure that required functionality
 * works for all cases.
 */
public class BookHashTableTest {

	// Default name of books data file or try books_clean.csv
	public static final String BOOKS = "books.csv";

	// Empty hash tables that can be used by tests
	static BookHashTable bookObject;
	static ArrayList<Book> bookTable;

	static final int INIT_CAPACITY = 2;
	static final double LOAD_FACTOR_THRESHOLD = 0.49;

	static Random RNG = new Random(0); // seeded to make results repeatable (deterministic)

	/** Create a large array of keys and matching values for use in any test */
	@BeforeAll
	public static void beforeClass() throws Exception {
		bookTable = BookParser.parse(BOOKS);
	}

	/** Initialize empty hash table to be used in each test */
	@BeforeEach
	public void setUp() throws Exception {
		bookObject = new BookHashTable(INIT_CAPACITY, LOAD_FACTOR_THRESHOLD);
	}

	/** Not much to do, just make sure that variables are reset */
	@AfterEach
	public void tearDown() throws Exception {
		bookObject = null;
	}

	private void insertMany(ArrayList<Book> bookTable) throws IllegalNullKeyException, DuplicateKeyException {
		for (int i = 0; i < bookTable.size(); i++) {
			bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
		}
	}

	/**
	 * IMPLEMENTED AS EXAMPLE FOR YOU Tests that a HashTable is empty upon
	 * initialization
	 */
	@Test
	public void test000_collision_scheme() {
		if (bookObject == null)
			fail("Gg");
		int scheme = bookObject.getCollisionResolutionScheme();
		if (scheme < 1 || scheme > 9)
			fail("collision resolution must be indicated with 1-9");
	}

	/**
	 * IMPLEMENTED AS EXAMPLE FOR YOU Tests that a HashTable is empty upon
	 * initialization
	 */
	@Test
	public void test000_IsEmpty() {
		// "size with 0 entries:"
		assertEquals(0, bookObject.numKeys());
	}

	/**
	 * IMPLEMENTED AS EXAMPLE FOR YOU Tests that a HashTable is not empty after
	 * adding one (key,book) pair
	 * 
	 * @throws DuplicateKeyException
	 * @throws IllegalNullKeyException
	 */
	@Test
	public void test001_IsNotEmpty() throws IllegalNullKeyException, DuplicateKeyException {
		bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
		String expected = "" + 1;
		// "size with one entry:"
		assertEquals(expected, "" + bookObject.numKeys());
	}

	/**
	 * IMPLEMENTED AS EXAMPLE FOR YOU Test if the hash table will be resized after
	 * adding two (key,book) pairs given the load factor is 0.49 and initial
	 * capacity to be 2.
	 */

	@Test
	public void test002_Resize() throws IllegalNullKeyException, DuplicateKeyException {
		bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
		int cap1 = bookObject.getCapacity();
		bookObject.insert(bookTable.get(1).getKey(), bookTable.get(1));
		int cap2 = bookObject.getCapacity();
		// "size with one entry:"
		assertTrue(cap2 > cap1 & cap1 == 2);
	}

	@Test
	public void test003_insert_many_check_capacity() {
		try {
			insertMany(bookTable);
			int cap = bookObject.getCapacity();
			assertEquals(cap, 24575);
		} catch (Exception e) {
			fail("exception thrown");
		}

	}

	@Test
	public void test004_insert_many_remove_many() {
		try {
			for (int i = 0; i < 101; i++) {
				bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
			}
			for (int i = 0; i < 101; i++) {
				bookObject.remove(bookTable.get(i).getKey());
			}
			assertEquals(bookObject.numKeys(), 0);
		} catch (Exception e) {
			fail("exception thrown");
		}
	}

	@Test
	public void test005_remove_decrements_numKeys() {
		try {
			for (int i = 0; i < 10; i++) {
				bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
			}
			int startKeyCount = bookObject.numKeys();
			bookObject.remove(bookTable.get(0).getKey());
			int newKeyCount = bookObject.numKeys();
			assertTrue(startKeyCount > newKeyCount);
		} catch (Exception e) {
			fail("exception thrown");
		}
	}

	@Test
	public void test006_insert_duplicate_key_throws_exception() {
		boolean thrown = false;
		try {
			bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
			bookObject.insert(bookTable.get(1).getKey(), bookTable.get(1));
			bookObject.insert(bookTable.get(2).getKey(), bookTable.get(2));
			bookObject.insert(bookTable.get(1).getKey(), bookTable.get(1));
			fail("Exception not thrown for duplicate key");
		} catch (DuplicateKeyException e) {
			thrown = true;
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
		assertTrue(thrown);
	}

	@Test
	public void test007_insert_null_throws_exception() {
		boolean thrown = false;
		try {
			bookObject.insert(null, bookTable.get(0));
			fail("Exception not thrown for null key insert");
		} catch (IllegalNullKeyException e) {
			thrown = true;
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
		assertTrue(thrown);
	}

	@Test
	public void test008_get_KeyNotFoundException() {
		boolean thrown = false;
		try {
			bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
			bookObject.insert(bookTable.get(1).getKey(), bookTable.get(1));
			bookObject.insert(bookTable.get(2).getKey(), bookTable.get(2));
			bookObject.get(bookTable.get(3).getKey());
			fail("Exception not thrown for unfound key insert");
		} catch (KeyNotFoundException e) {
			thrown = true;
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
		assertTrue(thrown);
	}

}
