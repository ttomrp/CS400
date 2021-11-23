import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphTest {
	Graph graph;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		graph = new Graph();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		graph = null;
	}

	@Test
	void test_empty_graph() {
		if (graph.size() != 0)
			fail("Size of epmty graph is not 0.");
	}

	@Test
	void test_remove_null_vertex() {
		try {
			graph.removeVertex("10");
		} catch (NullPointerException e) {
			// pass
		} catch (Exception e) {
			// unexpected case
			e.printStackTrace();
		}
	}

	@Test
	void test_add_null_vertex() {
		try {
			graph.addVertex(null);
		} catch (NullPointerException e) {
			// pass
		} catch (Exception e) {
			// unexpected case
			e.printStackTrace();
		}
	}

	@Test
	void test_empty_graph_order() {
		if (graph.order() != 0)
			fail("Order of empty graph is not 0.");
	}

	@Test
	void test_add_remove_vertex() {
		graph.addVertex("1");
		graph.removeVertex("1");
		if (graph.order() != 0)
			fail("The size and order should be 0, but isn't.");
	}

	@Test
	void test_add_vertices() {
		graph.addVertex("1");
		graph.addVertex("2");
		graph.addVertex("3");
		graph.addVertex("4");
		graph.removeVertex("1");
		graph.addVertex("5");
		graph.addVertex("6");
		graph.addVertex("7");
		graph.removeVertex("4");
		graph.addVertex("8");
		if (graph.order() != 6)
			fail("The size and order should be 0, but isn't.");
	}

	@Test
	void test_add_edge() {
		graph.addEdge("1", "2");
		if (graph.size() != 1 || graph.order() != 2)
			fail("The size should be 1 and order should be 2");
	}

	@Test
	void test_add_remove_edges() {
		graph.addEdge("1", "2");
		graph.addEdge("2", "3");
		graph.addEdge("2", "4");
		graph.addEdge("1", "4");
		graph.addEdge("3", "1");
		if (graph.size() != 5 || graph.order() != 4)
			fail("The size should be 5 and order should be 4");
	}

	@Test
	void test_remove_null_edge() {
		try {
			graph.removeEdge("10", "9");
		} catch (NullPointerException e) {
			// pass
		} catch (Exception e) {
			// unexpected case
			e.printStackTrace();
		}
	}

	@Test
	void test_getAdjacentVerticesOf() {
		graph.addEdge("A", "B");
		List<String> adjacent = graph.getAdjacentVerticesOf("A");
		if (adjacent.size() != 1 || !adjacent.get(0).equals("B"))
			fail("Dependency should be B.");
	}

	@Test
	void test_getAllVertices() {
		try {
			graph.addVertex("1");
			String vertices = graph.getAllVertices().toString();
			if (!vertices.equals(graph.getAllVertices().toString()))
				fail("Expected is 1.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
