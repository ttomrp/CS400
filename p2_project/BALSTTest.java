import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//@SuppressWarnings("rawtypes")
public class BALSTTest {

	protected BALSTADT<Integer, String> bst;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		bst = new BALST<Integer, String>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * CASE 123 Insert three values in sorted order and then check the root, left,
	 * and right keys to see if insert worked correctly.
	 */
	@Test
	void testBST_001_insert_sorted_order_simple() {
		try {
			bst.insert(10, "10");
			if (!bst.getKeyAtRoot().equals(10))
				fail("insert at root does not work");

			bst.insert(20, "20");
			if (!bst.getKeyOfRightChildOf(10).equals(20))
				fail("insert to right child of root does not work");

			bst.insert(30, "30");
			if (!bst.getKeyAtRoot().equals(20))
				fail("inserting 30 changed root");

			if (!bst.getKeyOfRightChildOf(20).equals(30))
				fail("inserting 30 as right child of 20");

			// IF rebalancing is working,
			// the tree should have 20 at the root
			// and 10 as its left child and 30 as its right child

			Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(20));
			Assert.assertEquals(bst.getKeyOfRightChildOf(20), Integer.valueOf(30));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(20), Integer.valueOf(10));

			bst.print();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * CASE 321 Insert three values in reverse sorted order and then check the root,
	 * left, and right keys to see if insert worked in the other direction.
	 */
	@Test
	void testBST_002_insert_reversed_sorted_order_simple() {
		try {
			bst.insert(30, "30");
			if (!bst.getKeyAtRoot().equals(30))
				fail("insert at root does not work");

			bst.insert(20, "20");
			if (!bst.getKeyOfLeftChildOf(30).equals(20))
				fail("insert to left child of root does not work");

			bst.insert(10, "10");
			if (!bst.getKeyAtRoot().equals(20))
				fail("inserting 10 changed root");

			if (!bst.getKeyOfLeftChildOf(20).equals(10))
				fail("inserting 10 as left child of 20");

			// IF rebalancing is working,
			// the tree should have 20 at the root
			// and 10 as its left child and 30 as its right child

			Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(20));
			Assert.assertEquals(bst.getKeyOfRightChildOf(20), Integer.valueOf(30));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(20), Integer.valueOf(10));

			bst.print();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * CASE 132 Insert three values so that rebalancing requires new key to be the
	 * "new" root of the rebalanced tree.
	 * 
	 * Then check the root, left, and right keys to see if insert occurred
	 * correctly.
	 */
	@Test
	void testBST_003_insert_smallest_largest_middle_order_simple() {
		try {
			bst.insert(10, "10");
			if (!bst.getKeyAtRoot().equals(10))
				fail("insert at root does not work");

			bst.insert(30, "30");
			if (!bst.getKeyOfRightChildOf(10).equals(30))
				fail("insert to right child of root does not work");

			bst.insert(20, "20");
			if (!bst.getKeyAtRoot().equals(20))
				fail("inserting 20 changed root");

			if (!bst.getKeyOfLeftChildOf(20).equals(10))
				fail("inserting 20 as left child of 30");

			// IF rebalancing is working,
			// the tree should have 20 at the root
			// and 10 as its left child and 30 as its right child

			Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(20));
			Assert.assertEquals(bst.getKeyOfRightChildOf(20), Integer.valueOf(30));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(20), Integer.valueOf(10));

			bst.print();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * CASE 312 Insert three values so that rebalancing requires new key to be the
	 * "new" root of the rebalanced tree.
	 * 
	 * Then check the root, left, and right keys to see if insert occurred
	 * correctly.
	 */
	@Test
	void testBST_004_insert_largest_smallest_middle_order_simple() {
		try {
			bst.insert(30, "30");
			if (!bst.getKeyAtRoot().equals(30))
				fail("insert at root does not work");

			bst.insert(10, "10");
			if (!bst.getKeyOfLeftChildOf(30).equals(10))
				fail("insert to left child of root does not work");

			bst.insert(20, "20");
			if (!bst.getKeyAtRoot().equals(20))
				fail("inserting 10 changed root");

			if (!bst.getKeyOfRightChildOf(20).equals(30))
				fail("inserting 20 as right child of 10");

			// the tree should have 30 at the root
			// and 10 as its left child and 20 as 10's right child

			Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(20));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(20), Integer.valueOf(10));
			Assert.assertEquals(bst.getKeyOfRightChildOf(20), Integer.valueOf(30));

			bst.print();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	// TODO: Add your own tests

	// Add tests to make sure that bst grows as expected.
	// Does it maintain it's balance?
	// Does the height of the tree reflect it's actual height
	// Use the traversal orders to check.

	// Can you insert many and still "get" them back out?

	// Does delete work?
	// If delete is not implemented, does calling it throw an
	// UnsupportedOperationException

	/**
	 * Insert several nodes and check that the tree contains a node with a searched
	 * key
	 */
	@Test
	void testBST_005_add_few_check_contains() {
		try {
			bst.insert(10, "10");
			bst.insert(5, "5");
			bst.insert(30, "30");
			bst.insert(20, "20");
			if (!bst.contains(20)) {
				fail("Should contain 20, but doesn't");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Insert several nodes and check that height is correct after rebalance
	 */
	@Test
	void testBST_006_getHeight() {
		try {
			bst.insert(10, "10");
			bst.insert(5, "5");
			bst.insert(30, "30");
			bst.insert(20, "20");
			bst.print();
			if (bst.getHeight() != 3) {
				fail("Height should be 3, but returned " + bst.getHeight());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Insert many nodes in unbalanced way. Insert method should cascade fixes up
	 * the tree on the last insert.
	 */
	@Test
	void testBST_007_insert_many_unbalanced_left_test_rebalancing() {
		try {
			bst.insert(10, "10");
			if (!bst.getKeyAtRoot().equals(10))
				fail("insert at root does not work");

			bst.insert(9, "9");
			if (!bst.getKeyOfLeftChildOf(10).equals(9))
				fail("insert 9 to left child of root does not work");

			bst.insert(8, "8");
			if (!bst.getKeyAtRoot().equals(9))
				fail("inserting 8 should change root");
			if (!bst.getKeyOfRightChildOf(9).equals(10))
				fail("Right child of root should be 10");

			bst.insert(7, "7");
			if (!bst.getKeyAtRoot().equals(9))
				fail("inserting 7 should not change root");
			if (!bst.getKeyOfLeftChildOf(8).equals(7))
				fail("insert to 7 left child of root does not work");

			bst.insert(6, "6");
			if (!bst.getKeyOfLeftChildOf(7).equals(6))
				fail("6 should be left child of 7");
			if (!bst.getKeyOfRightChildOf(7).equals(8))
				fail("8 should be right child of 7");

			bst.insert(5, "5");
			if (!bst.getKeyOfLeftChildOf(7).equals(6))
				fail("insert 5 should not change children of 7");
			if (!bst.getKeyOfRightChildOf(7).equals(8))
				fail("insert 5 should not change children of 7");

			bst.insert(4, "4");
			if (!bst.getKeyOfLeftChildOf(7).equals(5))
				fail("insert 4 should change right child of 7");
			if (!bst.getKeyOfRightChildOf(7).equals(8))
				fail("insert 4 should not change left child of 7");

			bst.insert(3, "3");
			if (!bst.getKeyAtRoot().equals(7))
				fail("inserting 3 should change root");
			if (!bst.getKeyOfLeftChildOf(7).equals(5))
				fail("5 should be left child of 7");
			if (!bst.getKeyOfRightChildOf(7).equals(9))
				fail("9 should be right child of 7");
			if (!bst.getKeyOfLeftChildOf(5).equals(4))
				fail("4 should be left child of 5");
			if (!bst.getKeyOfRightChildOf(5).equals(6))
				fail("6 should be right child of 5");
			if (!bst.getKeyOfLeftChildOf(9).equals(8))
				fail("8 should be left child of 9");
			if (!bst.getKeyOfRightChildOf(9).equals(10))
				fail("10 should be right child of 9");

			Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(7));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(7), Integer.valueOf(5));
			Assert.assertEquals(bst.getKeyOfRightChildOf(7), Integer.valueOf(9));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(5), Integer.valueOf(4));
			Assert.assertEquals(bst.getKeyOfRightChildOf(5), Integer.valueOf(6));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(9), Integer.valueOf(8));
			Assert.assertEquals(bst.getKeyOfRightChildOf(9), Integer.valueOf(10));

			bst.print();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Check that inserting null key throws IllegalNullKeyException
	 */
	@Test
	void testBST_008_insert_null_throws_exception() {
		boolean thrown = false;
		try {
			bst.insert(null, "null");
			fail("Exception not thrown for null key insert");
		} catch (IllegalNullKeyException e) {
			thrown = true;
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
		assertTrue(thrown);
	}

	/**
	 * inert many values in unbalanced way. Check that the get method can retrieve
	 * the values of those inserted nodes.
	 */
	@Test
	void testBST_009_insert_many_get_values() {
		try {
			for (int i = 0; i < 100; i++) {
				bst.insert(i, "value: " + i);
			}
			for (int i = 0; i < 100; i++) {
				bst.get(i);
				if (bst.get(i) == null) {
					fail("get method cannot find value of key i");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Check preorder traversal after balanced insert.
	 */
	@Test
	void testBST_010_preOrder_balanced_insert() {
		try {
			bst.insert(2, "2");
			bst.insert(1, "1");
			bst.insert(3, "3");

			List<Integer> correctOrder = new ArrayList<Integer>();
			correctOrder.add(2);
			correctOrder.add(1);
			correctOrder.add(3);

			List<Integer> testOrder = bst.getPreOrderTraversal();
			System.out.println("PreOrder: " + correctOrder);
			System.out.println("Returned: " + testOrder);
			assertEquals(correctOrder, testOrder);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Check postorder traversal after balanced insert.
	 */
	@Test
	void testBST_011_postOrder_balanced_insert() {
		try {
			bst.insert(2, "2");
			bst.insert(1, "1");
			bst.insert(3, "3");

			List<Integer> correctOrder = new ArrayList<Integer>();
			correctOrder.add(1);
			correctOrder.add(3);
			correctOrder.add(2);

			List<Integer> testOrder = bst.getPostOrderTraversal();
			System.out.println("PostOrder: " + correctOrder);
			System.out.println("Returned: " + testOrder);
			assertEquals(correctOrder, testOrder);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Check in-order traversal after balanced insert
	 */
	@Test
	void testBST_012_inOrder_balanced_insert() {
		try {
			bst.insert(2, "2");
			bst.insert(1, "1");
			bst.insert(3, "3");

			List<Integer> correctOrder = new ArrayList<Integer>();
			correctOrder.add(1);
			correctOrder.add(2);
			correctOrder.add(3);

			List<Integer> testOrder = bst.getInOrderTraversal();
			System.out.println("InOrder: " + correctOrder);
			System.out.println("Returned: " + testOrder);
			assertEquals(correctOrder, testOrder);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Check level-order traversal after balanced insert.
	 */
	@Test
	void testBST_013_levelOrder_balanced_insert() {
		try {
			bst.insert(2, "2");
			bst.insert(1, "1");
			bst.insert(3, "3");

			List<Integer> correctOrder = new ArrayList<Integer>();
			correctOrder.add(2);
			correctOrder.add(1);
			correctOrder.add(3);

			List<Integer> testOrder = bst.getLevelOrderTraversal();
			System.out.println("LevelOrder: " + correctOrder);
			System.out.println("Returned: " + testOrder);
			assertEquals(correctOrder, testOrder);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Check preorder traversal after unbalanced insert.
	 */
	@Test
	void testBST_014_preOrder_unbalanced_insert() {
		try {
			bst.insert(1, "1");
			bst.insert(2, "2");
			bst.insert(3, "3");

			List<Integer> correctOrder = new ArrayList<Integer>();
			correctOrder.add(2);
			correctOrder.add(1);
			correctOrder.add(3);

			List<Integer> testOrder = bst.getPreOrderTraversal();
			System.out.println("PreOrder: " + correctOrder);
			System.out.println("Returned: " + testOrder);
			assertEquals(correctOrder, testOrder);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Check postorder traversal after unbalanced insert.
	 */
	@Test
	void testBST_015_postOrder_unbalanced_insert() {
		try {
			bst.insert(1, "1");
			bst.insert(2, "2");
			bst.insert(3, "3");

			List<Integer> correctOrder = new ArrayList<Integer>();
			correctOrder.add(1);
			correctOrder.add(3);
			correctOrder.add(2);

			List<Integer> testOrder = bst.getPostOrderTraversal();
			System.out.println("PostOrder: " + correctOrder);
			System.out.println("Returned: " + testOrder);
			assertEquals(correctOrder, testOrder);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Check in-order traversal after unbalanced insert.
	 */
	@Test
	void testBST_016_inOrder_unbalanced_insert() {
		try {
			bst.insert(1, "1");
			bst.insert(2, "2");
			bst.insert(3, "3");

			List<Integer> correctOrder = new ArrayList<Integer>();
			correctOrder.add(1);
			correctOrder.add(2);
			correctOrder.add(3);

			List<Integer> testOrder = bst.getInOrderTraversal();
			System.out.println("InOrder: " + correctOrder);
			System.out.println("Returned: " + testOrder);
			assertEquals(correctOrder, testOrder);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Check level-order traversal after unbalanced insert.
	 */
	@Test
	void testBST_017_levelOrder_unbalanced_insert() {
		try {
			bst.insert(1, "1");
			bst.insert(2, "2");
			bst.insert(3, "3");

			List<Integer> correctOrder = new ArrayList<Integer>();
			correctOrder.add(2);
			correctOrder.add(1);
			correctOrder.add(3);

			List<Integer> testOrder = bst.getLevelOrderTraversal();
			System.out.println("LevelOrder: " + correctOrder);
			System.out.println("Returned: " + testOrder);
			assertEquals(correctOrder, testOrder);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Insert one node and remove it. Check that value is removed and size is 0.
	 */
	@Test
	void testBST_018_add_one_remove_one_check_size() {
		try {
			bst.insert(10, "10");
			bst.remove(10);
			if (bst.getKeyAtRoot() != null)
				fail("removing 10 should make root null");
			if (bst.numKeys() != 0) {
				fail("Size should be 0, but returned " + bst.numKeys());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Insert several nodes and remove a leaf node. Check size.
	 */
	@Test
	void testBST_019_add_few_remove_leaf() {
		try {
			bst.insert(10, "10");
			bst.insert(5, "5");
			bst.insert(20, "20");
			bst.remove(5);
			if (bst.numKeys() != 2) {
				fail("Size should be 0, but returned " + bst.numKeys());
			}
			if (!bst.getKeyAtRoot().equals(10))
				fail("removing 5 should not change root");
			if (!bst.getKeyOfLeftChildOf(10).equals(null))
				fail("left child of 10 should be null");
			if (!bst.getKeyOfRightChildOf(10).equals(20))
				fail("right child of 10 should be 20");

			Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(10));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(7), Integer.valueOf(null));
			Assert.assertEquals(bst.getKeyOfRightChildOf(7), Integer.valueOf(20));

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	/**
	 * Insert many values and remove a nonleaf node.
	 */
	@Test
	void testBST_020_insert_many_left_remove_nonleaf_node() {
		try {
			bst.insert(10, "10");
			bst.insert(9, "9");
			bst.insert(8, "8");
			bst.insert(7, "7");
			bst.insert(6, "6");
			bst.insert(5, "5");
			bst.insert(4, "4");
			bst.insert(3, "3");
			bst.remove(5);
			if (!bst.getKeyAtRoot().equals(7))
				fail("removing 5 should not change root");
			if (!bst.getKeyOfLeftChildOf(7).equals(6))
				fail("6 should be left child of 7");
			if (!bst.getKeyOfRightChildOf(7).equals(9))
				fail("9 should be right child of 7");
			if (!bst.getKeyOfLeftChildOf(6).equals(4))
				fail("4 should be left child of 5");

			Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(7));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(7), Integer.valueOf(6));
			Assert.assertEquals(bst.getKeyOfRightChildOf(7), Integer.valueOf(9));
			Assert.assertEquals(bst.getKeyOfLeftChildOf(6), Integer.valueOf(4));
			bst.print();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.getMessage());
		}
	}

}
