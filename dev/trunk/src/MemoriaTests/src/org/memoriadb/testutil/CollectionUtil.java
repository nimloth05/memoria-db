package org.memoriadb.testutil;

import java.util.*;

import junit.framework.Assert;

public class CollectionUtil {
  
  public static void assertIterable(Iterable<?> expected, Iterable<?> actual) {
     Iterator<?> iterator1 = expected.iterator();
     Iterator<?> iterator2 = actual.iterator();
     
     while (iterator1.hasNext()) {
       Assert.assertTrue("actualIterator has no more elements.",iterator2.hasNext());
       
       Object obj1 = iterator1.next();
       Object obj2 = iterator2.next();
       
       if (obj1.getClass().isArray()) {
         Assert.assertTrue(Arrays.deepEquals((Object[])obj1, (Object[])obj2));
         continue;
       }
       
       Assert.assertEquals(obj1, obj2);
     }
  }
  
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
