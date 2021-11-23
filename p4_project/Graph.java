import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Filename: Graph.java Project: p4 Authors:
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {
	LinkedList<String> vertices;
	LinkedList<LinkedList<String>> edges;
	int size;

	/*
	 * Default no-argument constructor
	 */
	public Graph() {
		vertices = new LinkedList<String>();
		edges = new LinkedList<LinkedList<String>>();
		size = 0;
	}

	/**
	 * Add new vertex to the graph.
	 *
	 * If vertex is null or already exists, method ends without adding a vertex or
	 * throwing an exception.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in
	 * the graph
	 */
	@Override
	public void addVertex(String vertex) {
		if (vertex == null || vertices.contains(vertex)) {
			return;
		} else {
			vertices.add(vertex);
			edges.add(new LinkedList<String>());
		}
	}

	/**
	 * Remove a vertex and all associated edges from the graph.
	 * 
	 * If vertex is null or does not exist, method ends without removing a vertex,
	 * edges, or throwing an exception.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in
	 * the graph
	 */
	@Override
	public void removeVertex(String vertex) {
		if (vertex == null || !vertices.contains(vertex)) {
			return;
		} else {
			int v = vertices.indexOf(vertex);
			vertices.remove(v);
			edges.remove(v);
			if (size != 0) {
				size -= edges.get(v).size() - 1;
			}
		}
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).contains(vertex)) {
				edges.get(i).remove(i);
				size--;
			}
		}
	}

	/**
	 * Add the edge from vertex1 to vertex2 to this graph. (edge is directed and
	 * unweighted) If either vertex does not exist, add vertex, and add edge, no
	 * exception is thrown. If the edge exists in the graph, no edge is added and no
	 * exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are in
	 * the graph 3. the edge is not in the graph
	 */
	@Override
	public void addEdge(String vertex1, String vertex2) {
		if (vertex1 == null || vertex2 == null) {
			return;
		}
		if (!vertices.contains(vertex1)) {
			this.addVertex(vertex1);
		}
		if (!vertices.contains(vertex2)) {
			this.addVertex(vertex2);
		}
		int v1 = vertices.indexOf(vertex1);
		if (!edges.get(v1).contains(vertex2)) {
			edges.get(v1).add(vertex2);
			size++;
		}
	}

	/**
	 * Remove the edge from vertex1 to vertex2 from this graph. (edge is directed
	 * and unweighted) If either vertex does not exist, or if an edge from vertex1
	 * to vertex2 does not exist, no edge is removed and no exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are in
	 * the graph 3. the edge from vertex1 to vertex2 is in the graph
	 */
	@Override
	public void removeEdge(String vertex1, String vertex2) {
		if (vertex1 == null || vertex2 == null) {
			return;
		}
		if (!vertices.contains(vertex1) || !vertices.contains(vertex2)) {
			return;
		}
		int v1 = vertices.indexOf(vertex1);
		if (!edges.get(v1).contains(vertex2)) {
			return;
		} else {
			edges.get(v1).remove(vertex2);
			size--;
		}
	}

	/**
	 * Returns a Set that contains all the vertices
	 * 
	 */
	@Override
	public Set<String> getAllVertices() {
		Set<String> vertexSet = new LinkedHashSet<String>();
		for (int i = 0; i < vertices.size(); i++) {
			vertexSet.add(vertices.get(i));
		}
		return vertexSet;
	}

	/**
	 * Get all the neighbor (adjacent) vertices of a vertex
	 *
	 */
	@Override
	public List<String> getAdjacentVerticesOf(String vertex) {
		if (vertex == null || !vertices.contains(vertex)) {
			return null;
		} else {
			int v = vertices.indexOf(vertex);
			return edges.get(v);
		}
	}

	/**
	 * Returns the number of edges in this graph.
	 */
	@Override
	public int size() {
		// int size = 0;
		// for (int i = 0; i < edges.size(); i++) {
		// size += edges.get(i).size();
		// }
		return this.size;
	}

	/**
	 * Returns the number of vertices in this graph.
	 */
	@Override
	public int order() {
		return vertices.size();
	}
}
