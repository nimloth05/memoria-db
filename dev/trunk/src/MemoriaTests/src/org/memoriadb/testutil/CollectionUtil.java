package org.memoriadb.testutil;

import java.util.*;

import junit.framework.Assert;

public class CollectionUtil {
  
  /**
   * 
   * @param expected
   * @param actual
   */
  public static void assertIterable(Iterable<?> expected, Iterable<?> actual) {
     Iterator<?> expectations = expected.iterator();
     Iterator<?> actuals = actual.iterator();
     
     while (expectations.hasNext()) {
       Assert.assertTrue("not enough actuals: " + expected + " != " + actual,actuals.hasNext());
       
       Object obj1 = expectations.next();
       Object obj2 = actuals.next();
       
       if (obj1.getClass().isArray()) {
         Assert.assertTrue(Arrays.deepEquals((Object[])obj1, (Object[])obj2));
         continue;
       }
       
       Assert.assertEquals(obj1, obj2);
     }
     
     Assert.assertFalse("too many actuals: " + expected + " != " + actual, actuals.hasNext());
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
