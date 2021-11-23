import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class DataStructureADTTest<T extends DataStructureADT<String, String>> {

	private T ds;

	protected abstract T createInstance();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		ds = createInstance();
	}

	@AfterEach
	void tearDown() throws Exception {
		ds = null;
	}

	@Test
	void test00_empty_ds_size() {
		// Insert nothing, test size is 0
		if (ds.size() != 0)
			fail("data structure should be empty, with size=0, but size=" + ds.size());
	}

	// TODO: review tests 01 - 04

	@Test
	void test01_insert_one() {
		// Insert one key/value pair into data structure, tests size is 1
		String key = "1";
		String value = "one";
		ds.insert(key, value);
		assert (ds.size() == 1);
	}

	@Test
	void test02_insert_remove_one_size_0() {
		// Insert one key/value pair, removes it, test size is 0
		String key = "1";
		String value = "one";
		ds.insert(key, value);
		assert (ds.remove(key)); // remove the key
		if (ds.size() != 0)
			fail("data structure should be empty, with size=0, but size=" + ds.size());
	}

	@Test
	void test03_duplicate_exception_thrown() {
		// Test Duplicate Key Exception
		String key = "1";
		String value = "one";
		ds.insert("1", "one");
		ds.insert("2", "two");
		try {
			ds.insert(key, value);
			fail("duplicate exception not thrown");
		} catch (RuntimeException re) {
		}
		assert (ds.size() == 2);
	}

	@Test
	void test04_remove_returns_false_when_key_not_present() {
		// Test removing non-existent key will return false, and removed key should not
		// exist
		String key = "1";
		String value = "one";
		ds.insert(key, value);
		assert (!ds.remove("2")); // remove non-existent key is false
		assert (ds.remove(key)); // remove existing key is true
		if (ds.get(key) != null)
			fail("get(" + key + ") returned " + ds.get(key) + " which should be removed");
	}

	// TODO: add tests 05 - 07 as described in assignment
	// TODO: add more tests of your own design to ensure that you can detect
	// implementation that fail
	// Tip: consider different numbers of inserts and removes and how different
	// combinations of insert and removes

	@Test
	void test05_insert_duplicate_values() {
		// Test if DS can have different keys with same value
		String value = "dupe";
		ds.insert("1", value);
		ds.insert("2", value);
		if (ds.size() != 2)
			fail("ds.size() should be 2, but ds.size() = " + ds.size());
	}

	@Test
	void test06_store_500_items() {
		// Test if DS can store at least 500 items
		for (int i = 0; i < 500; i++) {
			ds.insert(Integer.toString(i), "V" + i);
		}
		if (ds.size() != 500) {
			fail("DS only stored " + ds.size() + "items");
		}
	}

	@Test
	void test07_store_remove_500_items() {
		// Test if DS can store and remove 500 items
		for (int i = 0; i < 500; i++) {
			ds.insert(Integer.toString(i), "V" + i);
		}
		for (int j = 0; j < 500; j++) {
			assert (ds.remove(Integer.toString(j)));
		}
		if (ds.size() != 0) {
			fail("DS didn't remove all items.  Size is " + ds.size());
		}
	}

	@Test
	void test08_get_null_key_throws_exception() {
		// Test if .get(null) throws an exception
		ds.insert("1", "one");
		boolean thrown = false;
		try {
			ds.get(null);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	@Test
	void test09_remove_null_key_throws_exception() {
		// Test if .remove(null) throws an exception
		ds.insert("1", "one");
		boolean thrown = false;
		try {
			ds.remove(null);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	@Test
	void test10_insert_null_key_throws_exception() {
		// Test if .insert(null) throws an exception
		String key = null;
		String value = "one";
		boolean thrown = false;
		try {
			ds.insert(key, value);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	@Test
	void test11_reinsert_removed_key() {
		// Test if a removed item can be reinserted
		ds.insert("1", "one");
		ds.remove("1");
		try {
			ds.insert("1", "one");
		} catch (RuntimeException e) {
			fail("DS can't re-insert key after it was removed");
		}
	}

	@Test
	void test12_insert_null_value() {
		// Test if null values are allowed
		ds.insert("1", null);
		if (!ds.contains("1"))
			fail("DS did not insert key with null value");
	}

	@Test
	void test13_insert2_remove1_insert1_remove1_get_size() {
		ds.insert("1", "one");
		ds.insert("2", "two");
		ds.remove("2");
		ds.insert("3", "three");
		ds.remove("3");
		assertEquals(ds.size(), 1);
		;
	}
}
