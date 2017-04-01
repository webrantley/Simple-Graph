package edu.nyu.cs.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import edu.nyu.cs.graph.UndirectedGraph;

public class UndirectedGraphTest {
  UndirectedGraph<Integer> testGraph;
  
  @Before
  public void graphTestSetup() {
    testGraph = new UndirectedGraph<Integer>();
  }
  
  @Test
  public void testSecondConstructor() {
    testGraph.addVertices(Arrays.asList(1, 2, 3));
    testGraph.addEdge(1, 2, "test");
    testGraph.addEdge(2, 3, "test");
    UndirectedGraph<Integer> copiedGraph =
        new UndirectedGraph<Integer>(testGraph);
    
    Map<Integer, List<Integer>> adjMap = copiedGraph.getAdjacencyMap();
    
    for (Integer vertex : adjMap.keySet()) {
      for (Integer neighbor : copiedGraph.getNeighbors(vertex)) {
        assertTrue(testGraph.hasEdge(vertex, neighbor));
      }
    }
    
    testGraph.addEdge(1, 3, "shouldn't be in the new graph");
    assertFalse(copiedGraph.hasEdge(1, 3));
  }

  @Test(expected = NullPointerException.class)
  public void testAddVertex_null() {
    testGraph.addVertex(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetNeighbors_empty() {
    List<Integer> nullList = testGraph.getNeighbors(1);
    nullList.isEmpty();
  }
  
  @Test
  public void testAddVertex() {
    assertTrue(testGraph.addVertex(10));
    List<Integer> s = testGraph.getVertices();
    assertTrue(s.contains(10));
    assertTrue(s.size() == 1);
  }
  
  @Test public void testAddVertices() {
    testGraph.addVertex(10);
    testGraph.addVertex(15);
    testGraph.addVertex(20);
    
    UndirectedGraph<Integer> secondGraph = new UndirectedGraph<Integer>();
    secondGraph.addVertices(testGraph.getVertices());
    assertTrue(secondGraph.getVertices().size() == 3);
    assertTrue(secondGraph.containsVertex(10));
    assertTrue(secondGraph.containsVertex(15));
    assertTrue(secondGraph.containsVertex(20));
    
    testGraph.addEdge(10, 15, "Shouldn't be in the other one");
    assertFalse(secondGraph.hasEdge(10, 15));
  }
  
  @Test(expected = NullPointerException.class)
  public void testAddEdge_null() {
    testGraph.addEdge(null, null, null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddEdge_ItemNotInGraph() {
    testGraph.addEdge(10, 15, "Should throw an exception");
  }
  
  @Test
  public void testAddEdge() {
    testGraph.addVertex(10);
    testGraph.addVertex(15);
    assertEquals(testGraph.addEdge(10, 15, "FirstValue"), null);
    assertEquals(testGraph.addEdge(15, 10, "Overwriting old edge"),
        "FirstValue");
    assertEquals(testGraph.getEdge(10, 15), "Overwriting old edge");
  }
  
  @Test
  public void testHasEdge() {
    testGraph.addVertex(10);
    testGraph.addVertex(15);
    testGraph.addEdge(10, 15, "Hello");
    assertTrue(testGraph.hasEdge(10, 15));
    assertTrue(testGraph.hasEdge(15, 10));
    assertFalse(testGraph.hasEdge(10, 10));
  }
  
  @Test(expected = NullPointerException.class)
  public void testContainsVertex_null() {
    testGraph.containsVertex(null);
  }
  
  @Test
  public void testContainsVertex() {
    testGraph.addVertex(10);
    assertTrue(testGraph.containsVertex(10));
    assertFalse(testGraph.containsVertex(1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveEdge_doesntExist() {
    testGraph.removeEdge(10, 15);
  }
  
  @Test
  public void testRemoveEdge() {
    testGraph.addVertex(10);
    testGraph.addVertex(15);
    testGraph.addEdge(10, 15, "Hello");
    assertEquals(testGraph.removeEdge(10, 15), "Hello");
    assertFalse(testGraph.hasEdge(10,  15));
    assertFalse(testGraph.hasEdge(15,  10));
  }
  
  @Test
  public void testRemoveVertex() {
    testGraph.addVertices(Arrays.asList(1, 2, 3));
    testGraph.addEdge(1, 1, "self-edge");
    testGraph.addEdge(1, 2, "two and one");
    testGraph.addEdge(1, 3, "one and three");
    testGraph.removeVertex(1);
    assertFalse(testGraph.hasEdge(1, 1));
    assertFalse(testGraph.hasEdge(1, 2));
    assertFalse(testGraph.hasEdge(1, 3));
    assertFalse(testGraph.areAdjacentForTesting(2, 1));
    assertFalse(testGraph.areAdjacentForTesting(3, 1));
    assertFalse(testGraph.containsVertex(1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBFSIter_empty() {
    Iterator<Integer> iter = testGraph.bfsIterator(1);
    iter.next();
  }
  
  @Test(expected = NoSuchElementException.class)
  public void testBFSIter_throwsException() {
    testGraph.addVertex(1);
    Iterator<Integer> iter = testGraph.bfsIterator(1);
    assertEquals(iter.next(), (Integer) 1);
    assertFalse(iter.hasNext());
    iter.next(); 
  }
  
  @Test
  public void testBFSIter() {
    testGraph.addVertices(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    testGraph.addEdge(1, 1, "self-edge");
    testGraph.addEdge(1, 2, "test");
    testGraph.addEdge(1, 3, "test");
    testGraph.addEdge(1, 4, "test");
    testGraph.addEdge(1, 5, "test");
    testGraph.addEdge(2, 6, "test");
    testGraph.addEdge(3, 7, "test");
    testGraph.addEdge(4, 8, "test");
    testGraph.addEdge(5, 9, "test");

    Iterator<Integer> iter = testGraph.bfsIterator(1);
    assertTrue(matchSequence(iter, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    
    Iterator<Integer> iter2 = testGraph.bfsIterator(10);
    assertTrue(matchSequence(iter2, 10));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDFSIter_empty() {
    Iterator<Integer> iter = testGraph.dfsIterator(1);
    iter.next();
  }
  
  @Test(expected = NoSuchElementException.class)
  public void testDFSIter_throwsException() {
    testGraph.addVertex(1);
    Iterator<Integer> iter = testGraph.dfsIterator(1);
    assertEquals(iter.next(), (Integer) 1);
    assertFalse(iter.hasNext());
    iter.next(); 
  }
  
  @Test
  public void testDFSIter() {
    testGraph.addVertices(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    testGraph.addEdge(1, 2, "test");
    testGraph.addEdge(1, 3, "test");
    testGraph.addEdge(1, 4, "test");
    testGraph.addEdge(1, 5, "test");
    testGraph.addEdge(2, 6, "test");
    testGraph.addEdge(3, 7, "test");
    testGraph.addEdge(4, 8, "test");
    testGraph.addEdge(5, 9, "test");
    
    Iterator<Integer> iter = testGraph.dfsIterator(1);
    assertTrue(matchSequence(iter, 1, 2, 6, 3, 7, 4, 8, 5, 9));
    
    Iterator<Integer> iter2 = testGraph.dfsIterator(10);
    assertTrue(matchSequence(iter2, 10));
  }
  
  //Doesn't rely on subtyping or mutation to Object[] to perform, so it's safe
  //to suppress warnings here
  private <T> boolean matchSequence(Iterator<T> iter,
      @SuppressWarnings("unchecked") T... sequence) {
    for (T item : sequence) {
      T otherItem = iter.next();
      if (!otherItem.equals(item)) {
        return false;
      }
    }
    return !iter.hasNext();
  }
}
