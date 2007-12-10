package org.memoriadb.test.javaapi;

import java.util.*;

import org.memoriadb.test.testclasses.WrongHashCode;

import junit.framework.TestCase;

/**
 * Test for the IdentityHashMap from the collection framework.
 * 
 * @author sandro
 *
 */
public class IdentityHashMapTest extends TestCase {
  
  private IdentityHashMap<Object, Object> fMap;

  public void test_containsKey() {
    WrongHashCode obj1 = new WrongHashCode();
    WrongHashCode obj2 = new WrongHashCode();
    fMap.put(obj1, "1");
    assertTrue(fMap.containsKey(obj1));
    assertFalse(fMap.containsKey(obj2));
  }
  
  public void test_containsKey_fromKeySet() {
    WrongHashCode obj1 = new WrongHashCode();
    WrongHashCode obj2 = new WrongHashCode();
    fMap.put(obj1, "1");
    Set<Object> keySet = fMap.keySet();
    assertTrue(keySet.contains(obj1));
    assertFalse(keySet.contains(obj2));
  }
  
  public void test_containsValue() {
    WrongHashCode obj1 = new WrongHashCode();
    fMap.put(obj1, "1");
    assertTrue(fMap.containsValue("1"));
    assertFalse(fMap.containsValue("2"));
  }
  
  public void test_keySet_removeAll() {
    WrongHashCode obj1 = new WrongHashCode();
    WrongHashCode obj2 = new WrongHashCode();
    fMap.put(obj1, "1");
    fMap.put(obj2, "2");
    
    Set<Object> keySet = fMap.keySet();
    
    Collection<Object> removeCollection = new ArrayList<Object>(2);
    removeCollection.add(new WrongHashCode());
    removeCollection.add(new WrongHashCode());
    
    assertFalse(keySet.containsAll(removeCollection));
    
    keySet.removeAll(removeCollection);
    assertEquals(0, fMap.size());
    
    try {
      assertTrue(keySet.addAll(removeCollection));
      fail("AddAll is not possible on a keySet");
    }
    catch (UnsupportedOperationException e) {
      //
    }
  }

  @Override
  protected void setUp() throws Exception {
    fMap = new IdentityHashMap<Object, Object>();
  }
  
}
