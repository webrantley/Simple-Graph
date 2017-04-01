package edu.nyu.cs.graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

/***
 * Breadth First Search iterator for graphs. Accepts a graph and a starting
 * vertex and can iterate over the graph in a breadth first order.
 * @author William Brantley
 *
 * @param <T> 
 */
class BreadthFirstIterator<T> implements Iterator<T> {
  //I think these iterators belong outside of the UndirectedGraph class.
  //They are reusable code that could be reused if a directed Graph was
  //implemented in this package.
  private final Set<T> visitedVertices;
  private final Queue<T> queue;
  private final UndirectedGraph<T> graph;
  
  BreadthFirstIterator(UndirectedGraph<T> graph, T firstNode) {
    visitedVertices = new HashSet<T>();
    this.graph = graph;
    queue = new LinkedList<T>();
    queue.add(firstNode);
  }

  @Override
  public boolean hasNext() {
    return !queue.isEmpty();
  }

  @Override
  public T next() {
    if(!hasNext()) {
      throw new NoSuchElementException("No more elements in the Iterator");
    }
    T nextElement = queue.remove();
    visitedVertices.add(nextElement);
    for (T neighbor : graph.getNeighbors(nextElement)) {
      if (!visitedVertices.contains(neighbor)) {
        queue.add(neighbor);
        visitedVertices.add(neighbor);
      }
    }
    return nextElement;
  }
}