package org.memoriadb.testutil;

import java.util.*;

import junit.framework.Assert;

public class Collections {
  
  /**
   * Asserts that all expected objects are contained in the actual collection. No order is considered.
   */
  public static void containsAll(Iterable<?> actual, Object... expected) {
    Set<Object> set = new HashSet<Object>();
    for(Object o: actual) {
      set.add(o);
    }
    
    for(Object obj: expected) {
      Assert.assertTrue("Missing: " + obj, set.remove(obj));
    }
    
    Assert.assertTrue("Unexpected objects: " + set, set.isEmpty());
  }
  
}
