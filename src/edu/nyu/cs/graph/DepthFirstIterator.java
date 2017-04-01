package edu.nyu.cs.graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

/***
 * Depth First Search iterator for graphs. Accepts a graph and a starting
 * vertex and can iterate over the graph in a depth first order.
 * @author William Brantley
 *
 * @param <T> 
 */
class DepthFirstIterator<T> implements Iterator<T> {
  Set<T> visitedVertices;
  Stack<T> stack;
  UndirectedGraph<T> graph;
  
  DepthFirstIterator(UndirectedGraph<T> graph, T startingVertex) {
    visitedVertices = new HashSet<T>();
    this.graph = graph;
    stack = new Stack<T>();
    stack.push(startingVertex);
    visitedVertices.add(startingVertex);
  }
  
  @Override
  public boolean hasNext() {
    return !stack.empty();
  }

  @Override
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more elements in the Iterator");
    }
    T nextElement = stack.peek();
    while(!stack.empty()) {
      T currentNode = stack.peek();
      for (T neighbor : graph.getNeighbors(currentNode)) {
        if (!visitedVertices.contains(neighbor)) {
          stack.push(neighbor);
          visitedVertices.add(neighbor);
          return nextElement;
        }
      }
      stack.pop();
    }
    return nextElement;
  }
}
