package edu.nyu.cs.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/***
 * Undirected graph implementation. Nodes must be non-null and unique.
 * There can not be multiple edges between the same pair of nodes (i.e. not a
 * multigraph). Note that this class is not thread-safe, and is intended to be
 * used by only one thread. The edges between graphs store a String to
 * represent the edge, which can be recalled using getEdge(). The value of the
 * edges can be updated once. There is no explicit vertex class in this
 * package. If the word vertex is used it means the value added to the graph.
 * Unconnected subgraphs are also allowed in this class.
 * @author William Brantley
 *
 * @param <T> Type of vertex to be stored
 */
public class UndirectedGraph<T> {
  private final Map<T, List<T>> adjacencyMap;
  private final UndirectedEdgeMap<T, String> edgeMap;
  
  public UndirectedGraph() {
    adjacencyMap = new HashMap<T, List<T>>();
    edgeMap = new UndirectedEdgeMap<T, String>();
  }
  
  public UndirectedGraph(UndirectedGraph<T> otherGraph) {
    this.adjacencyMap = otherGraph.getAdjacencyMap();
    this.edgeMap = new UndirectedEdgeMap<T, String>(otherGraph.edgeMap);
  }
  
  /***
   * Returns whether there are any vertices in the graph.
   * @return returns true if there is at least one vertex in the graph
   */
  public boolean isEmpty() {
    return adjacencyMap.isEmpty();
  }
  
  /***
   * Helper method to check if either of a pair of vertices are null. Implemented
   * to improve code readability. Use if adding a method to which two vertices are
   * passed. 
   * @param vertex1 First Vertex to check
   * @param vertex2 Second Vertex to check
   */
  private void nullEdgeCheck(T vertex1, T vertex2) {
    if (vertex1 == null) {
      throw new NullPointerException("First vertex is null");
    } if (vertex2 == null) {
      throw new NullPointerException("Second vertex is null");
    }
  }
  
  /***
   * Returns a list of the neighbors of the passed vertex, null if the vertex
   * isn't in the graph. Use in conjunction with containsVertex() to avoid
   * exceptions at runtime.
   * @param vertex Value of the vertex whose neighbors you're looking for.
   * @return List of the neighbors of the argument.
   */
  public List<T> getNeighbors(T vertex) {
    if (vertex == null) {
      throw new NullPointerException("Vertex passed is null");
    } if (!this.containsVertex(vertex)) {
      throw new IllegalArgumentException("Vertex not in the graph");
    }
    return new ArrayList<T>(adjacencyMap.get(vertex));
  }
  
  /***
   * Add a vertex to the graph. Since vertices must be unique, the method
   * will return false if the vertex already exists in the graph, but true
   * if the vertex didn't exists and was newly added.
   * @param vertex Vertex to add to the.
   * @return True if the vertex was added to the graph and false if it was
   *     already in the graph
   */
  public boolean addVertex(T vertex) {
    if (vertex == null) {
      throw new NullPointerException("New Vertex value is null");
    }
    if (adjacencyMap.containsKey(vertex)) {
      return false;
    }
    adjacencyMap.put(vertex, new ArrayList<T>());
    return true;
  }
  
  /***
   * Adds multiple vertices to the graph at once. Be aware that this method 
   * does not offer the same granularity of control as adding all the vertices
   * one at a time. For instance, you will not be able to know whether an
   * element fails because it already exists like you would with addVertex.
   * Also, upon failure, you won't know exactly which element of the collection
   * is null. However, it can be used in conjunction with vertexSet to create
   * add all the vertices of another graph without the edges.
   * 
   * @param c Collection of vertices you'd like to add to the graph
   */
  public void addVertices(Collection<T> c) {
    for (T vertex : c) {
      addVertex(vertex);
    }
  }

  /***
   * Adds or updates edge between two vertices in the graph. Will throw an
   * exception if passed a null vertex or if the vertex is not in the graph.
   * Again, you can use the containsVertex method to avoid runtime exceptions.
   * Will return null if this is a new edge, or the old value of the edge if
   * you are updating the value. Vertex order does not matter.
   * @param vertex1 First vertex
   * @param vertex2 Second vertex
   * @param message Value stored representing the edge.
   * @return null if this is a new edge, or the old value of the edge if
   *     you are updating the value;
   */
  public String addEdge(T vertex1, T vertex2, String message) {
    nullEdgeCheck(vertex1, vertex2);
    if (message == null) {
      throw new NullPointerException("Edge message is null");
    } if (!adjacencyMap.containsKey(vertex1)) {
      throw new IllegalArgumentException("First vertex isn't in the graph");
    } if (!adjacencyMap.containsKey(vertex2)) {
      throw new IllegalArgumentException("Second vertex isn't in the graph");
    }
    String oldEdgeValue = edgeMap.addEdge(vertex1, vertex2, message);
    updateAdjacencyMapNewEdge(vertex1, vertex2, oldEdgeValue);
    return oldEdgeValue;
  }
  
  /***
   * Helper method to separate and improve readability of the addEdge method.
   * @param vertex1 First vertex to update
   * @param vertex2 Second vertex to update
   * @param oldEdge Value of the old edge between the vertices or null if this
   *     is a new edge.
   */
  private void updateAdjacencyMapNewEdge(T v1, T v2, String oldEdge) {
    // Only update if there wasn't an edge between the two elements before
    if (oldEdge == null) {
      // if both vertices are the same, only update once to avoid 
      // adding it to the adjacency list twice
      if (v1.equals(v2)) {
        adjacencyMap.get(v1).add(v2);
      } else {
        adjacencyMap.get(v1).add(v2);
        adjacencyMap.get(v2).add(v1);
      }
    }
  }
  
  /***
   * Returns whether an edge exists between two vertices. Vertex order does
   * not matter.
   * @param vertex1 First vertex of the vertex pair
   * @param vertex2 Second vertex of teh vertex pair
   * @return true if an edge exists between the two vertices, false if not
   */
  public boolean hasEdge(T vertex1, T vertex2) {
    nullEdgeCheck(vertex1, vertex2);
    return edgeMap.containsEdge(vertex1, vertex2);
  }
  
  /***
   * Returns the value of an edge between two vertices. Vertex order does not
   * matter. Throws an exception if either of the vertices passed are null.
   * Throws an IllegalArgumentException if there doesn't exists an edge between
   * the vertices. Use hasEdge() to avoid runtimeExceptions.
   * @param vertex1 First vertex of the edge pair
   * @param vertex2 Second vertex of the edge pair
   * @return Value of the String associated with the edge between the vertices
   */
  public String getEdge(T vertex1, T vertex2) {
    nullEdgeCheck(vertex1, vertex2);
    if (edgeMap.getEdge(vertex1, vertex2) == null) {
      throw new IllegalArgumentException("The vertices aren't connected");
    }
    return edgeMap.getEdge(vertex1, vertex2);
  }
  
  /***
   * Removes vertex from the graph if it is in the graph and all edges
   * associated with that vertex. Throws an exception if the argument doesn't
   * exist in the graph, so if you're unsure if it exists in the graph, use
   * containsVertex() to avoid a runtime exception. 
   * @param vertex Vertex to be removed from the graph
   */
  public void removeVertex(T vertex) {
    if (vertex == null) {
      throw new NullPointerException("Vertex passed is null");
    } if (!adjacencyMap.containsKey(vertex)) {
      throw new NoSuchElementException("Vertex is not in the graph");
    }
    
    List<T> adjacentValues = adjacencyMap.get(vertex);
    for (T neighbor : adjacentValues) {
      if (!neighbor.equals(vertex)) {
        adjacencyMap.get(neighbor).remove(vertex);
      }
      edgeMap.removeEdge(vertex, neighbor);
    }
    adjacencyMap.remove(vertex); 
  }
  
  /***
   * Removes an edge between two vertices. Order does not matter. If the edge
   * didn't exist, an exception will be thrown. If a vertex doesn't exist in
   * the graph, an exception will be thrown. Again, if you aren't certain that
   * the vertices exist in the graph, use the hasVertex() and hasEdge() methods
   * to avoid runtime errors.
   * @param vertex1 First vertex of the edge pair
   * @param vertex2 Second vertex of the edge pair
   * @return
   */
  public String removeEdge(T vertex1, T vertex2) {
    nullEdgeCheck(vertex1, vertex2);
    if (!adjacencyMap.containsKey(vertex1)) {
      throw new IllegalArgumentException("First vertex isn't in the graph");
    } if (!adjacencyMap.containsKey(vertex2)) {
      throw new IllegalArgumentException("Second vertex isn't in the graph");
    } if (edgeMap.getEdge(vertex1, vertex2) == null) {
      throw new IllegalArgumentException("The vertices aren't connected");
    } 
    return edgeMap.removeEdge(vertex1, vertex2);
  }
  
  /***
   * Returns true a vertex is present in the graph, false otherwise
   * @param value Vertex that may or may not be in the graph
   * @return Returns true a vertex is present in the graph, false otherwise
   */
  public boolean containsVertex(T vertex) {
    if (vertex == null) {
      throw new NullPointerException("Vertex passed is null");
    }
    return adjacencyMap.containsKey(vertex);
  }
  
  /***
   * Returns List of all vertices in the graph
   * @return Returns List of all vertices in the graph
   */
  public List<T> getVertices() {
    return new ArrayList<T>(adjacencyMap.keySet());
  }
  
  /***
   * Returns a copy of the adjacency map to the user. The key is the vertex
   * in the graph and the value is a list of elements adjacent to it.
   * @return Adjacency Map for the graph
   */
  public Map<T, List<T>> getAdjacencyMap() {
    Map<T, List<T>> copiedMap = new HashMap<T, List<T>>();
    for (T key : adjacencyMap.keySet()) {
      List<T> neighbors = adjacencyMap.get(key);
      copiedMap.put(key, new ArrayList<T>(neighbors));
    }
    return copiedMap;
  }
  
  /***
   * Returns BreadthFirstSearch iterator starting from the provided vertex. 
   * There is no guarantee as to the specific order in which the vertices will
   * return, only that the order will be a breadth first order. An exception
   * will be thrown if the vertex provided is null or if the vertex doesn't
   * exist in the map. 
   * @param startingVertex First vertex in the traversal
   * @return Breadth-first iterator of the vertices in the graph
   */
  public Iterator<T> bfsIterator(T startingVertex) {
    if (startingVertex == null) {
      throw new NullPointerException("Starting vertex is null");
    } if (!containsVertex(startingVertex)) {
      throw new IllegalArgumentException("Vertix is not in the graph");
    }
    return new BreadthFirstIterator<T>(this, startingVertex);
  }
  
  /***
   * Returns DepthFirstSearch iterator starting from the provided vertex. 
   * There is no guarantee as to the specific order in which the vertices will
   * return, only that the order will be a depth first order. An exception
   * will be thrown if the vertex provided is null or if the vertex doesn't
   * exist in the map. 
   * @param startingVertex First vertex in the traversal
   * @return Depth-first iterator of the vertices in the graph
   */
  public Iterator<T> dfsIterator(T startingVertex) {
    if (startingVertex == null) {
      throw new NullPointerException("Starting vertex is null");
    } if (!containsVertex(startingVertex)) {
      throw new IllegalArgumentException("Vertix is not in the graph");
    }
    return new DepthFirstIterator<T>(this, startingVertex);
  }
  
  /***
   * Testing method to check if adjacency mapping is properly maintained
   * when deleting nodes. Returns true if the from vertex is connected
   * in the map to the to vertex.
   * @param from Vertex we're starting from
   * @param to Vertex we're pointing to
   * @return true if from is connected to to in the adjacency map
   */
  boolean areAdjacentForTesting(T from, T to) {
    nullEdgeCheck(to, from);
    return adjacencyMap.get(from).contains(to);
  }
  
  @Override
  public String toString() {
    StringBuilder graphAsString = new StringBuilder();
    for (T vertex : adjacencyMap.keySet()) {
      graphAsString.append(vertex + ": \n");
      for (T neighbor : getNeighbors(vertex)) {
        graphAsString.append(" -- " + 
            getEdge(vertex, neighbor) + " -- " + neighbor + "\n");
      }
    }
    return graphAsString.toString();
  }
}