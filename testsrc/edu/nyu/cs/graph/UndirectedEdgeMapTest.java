package edu.nyu.cs.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.nyu.cs.graph.UndirectedEdgeMap;

public class UndirectedEdgeMapTest {
  
  UndirectedEdgeMap<Integer, String> testMap;

  @Before
  public void setup() {
    testMap = new UndirectedEdgeMap<Integer, String>();
  }
  
  @Test
  public void testCopyConstructor() {
    testMap.addEdge(1, 2, "First Graph Edge");
    
    UndirectedEdgeMap<Integer, String> copiedMap =
        new UndirectedEdgeMap<Integer, String>(testMap);
    
    assertTrue(copiedMap.containsEdge(1, 2));
    assertTrue(copiedMap.containsEdge(2, 1));
    testMap.addEdge(1, 2, "Modified Edge");
    assertEquals(copiedMap.getEdge(1, 2), "First Graph Edge");
  }
  
  @Test
  public void testAddEdge() {
    assertFalse(testMap.containsEdge(1, 2));
    assertEquals(testMap.addEdge(1, 2, "Old Edge"), null);
    assertTrue(testMap.containsEdge(2, 1));
    assertEquals(testMap.addEdge(2, 1, "New Edge"), "Old Edge");
    assertTrue(testMap.containsEdge(1, 2));
  }
  
  @Test
  public void testGetEdge() {
    testMap.addEdge(1, 2, "Edge from 1 to 2");
    assertEquals(testMap.getEdge(1, 2), "Edge from 1 to 2");
    assertEquals(testMap.getEdge(1, 1), null);
  }
  
  @Test
  public void testRemoveEdge() {
    testMap.addEdge(1, 2, "1 to 2");
    assertEquals(testMap.removeEdge(2, 1), "1 to 2");
    assertFalse(testMap.containsEdge(1, 2));
    assertEquals(testMap.getEdge(1, 2), null);
  }
}
