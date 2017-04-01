package edu.nyu.cs.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/***
 * Map used for undirected graphs to store and recall edges between two
 * vertices of an undirected graph. This Map does not support multigraphs,
 * but does not, by necessity, require the user to input an edge in an certain
 * order and recall that edge by using the same order. For example, if an edge
 * is stored by addEdge(V1, V2, Edge), you can perform any of the methods with
 * order (V1, V2) or (V2, V1) in association with that edge.
 * @author William Brantley
 *
 * @param <K> Type associated with the vertices
 * @param <V> Type associated with the edges
 */
class UndirectedEdgeMap<K, V> {
  private final Map<Set<K>, V> edgeSetToEdgeMap;
  
  UndirectedEdgeMap() {
    edgeSetToEdgeMap = new HashMap<Set<K>, V>();
  }
  
  UndirectedEdgeMap(UndirectedEdgeMap<K,V> edgeMap) {
    this.edgeSetToEdgeMap = new HashMap<Set<K>, V>(edgeMap.edgeSetToEdgeMap);
  }
  
  /***
   * Returns whether there exists an edge between two vertices.
   * @param vertex1 First vertex
   * @param vertex2 Second vertex
   * @return Whether an edge exists between the vertices
   */
  boolean containsEdge(K vertex1, K vertex2) {
    Set<K> edgeSet = new HashSet<K>();
    edgeSet.add(vertex1);
    edgeSet.add(vertex2);
    return edgeSetToEdgeMap.containsKey(edgeSet);
  }
  
  /***
   * Returns value associated with edge between two vertices, or null if there
   * isn't an edge between the two vertices. 
   * @param vertex1 First vertex
   * @param vertex2 Second vertex
   * @return Value associated with the edge between the two vertices, null if
   *     there isn't one.
   */
  V getEdge(K vertex1, K vertex2) {
    Set<K> edgeSet = new HashSet<K>();
    edgeSet.add(vertex1);
    edgeSet.add(vertex2);
    return edgeSetToEdgeMap.get(edgeSet);
  }
  
  /***
   * Adds edge between two vertices and associates an edge between them.
   * Returns the old value associated with the edge if there is one, and
   * null otherwise.
   * @param vertex1 First vertex
   * @param vertex2 Second vertex
   * @param edge Value associated with the edge
   * @return Old value associated with the edge between the two vertices, null
   *     if there isn't one.
   */
  V addEdge(K vertex1, K vertex2, V edge) {
    Set<K> edgeSet = new HashSet<K>();
    edgeSet.add(vertex1);
    edgeSet.add(vertex2);
    return edgeSetToEdgeMap.put(edgeSet, edge);
  }
  
  /***
   * Removes edge between two vertices. Returning the previous value associated
   * with the edge, or null if there wasn't one.
   * @param vertex1 First vertex
   * @param vertex2 Second vertex
   * @return Old value associated with the edge between the two vertices, null
   *     if there isn't one.
   */
  V removeEdge(K vertex1, K vertex2) {
    Set<K> edgeSet = new HashSet<K>();
    edgeSet.add(vertex1);
    edgeSet.add(vertex2);
    return edgeSetToEdgeMap.remove(edgeSet);
  }
}