package org.memoriadb.test.javaapi;

import java.util.*;

import junit.framework.TestCase;

public class SetTest extends TestCase {
  
  public void test_hashCode() {
    Set<Integer> set = new HashSet<Integer>();
    
    for(int i = 0; i < 10; ++i) {
      set.add(i*3);
    }
    
    Set<Integer> set2 = new HashSet<Integer>();
    set2.addAll(set);
    
    assertEquals(set.hashCode(), set2.hashCode());
  }
  
  public void test_sets_in_sets_with_mixed_types_but_same_hashCode() {
    Set<Integer> subSet1 = new HashSet<Integer>();
    subSet1.add(1);
    subSet1.add(2);
    
    Set<Short> subSet2 = new HashSet<Short>();
    subSet2.add(Short.valueOf("1"));
    subSet2.add(Short.valueOf("2"));
    
    Set<Collection<?>> mainSet = new HashSet<Collection<?>>();
    mainSet.add(subSet1);
    mainSet.add(subSet2);
    
    assertEquals(subSet1.hashCode(), subSet2.hashCode());
    assertFalse(subSet1.equals(subSet2));
    
    assertEquals(2, mainSet.size());
  }
  
  public void test_with_multiple_empty_sets() {
    Set<Integer> subSet1 = new HashSet<Integer>();
    Set<Integer> subSet2 = new HashSet<Integer>();
    
    Set<Collection<?>> mainSet = new HashSet<Collection<?>>();
    mainSet.add(subSet1);
    mainSet.add(subSet2);
    
    assertEquals(1, mainSet.size());
  }
  
  public void test_with_multiple_sets() {
    Set<Integer> subSet1 = new HashSet<Integer>();
    subSet1.add(1);
    subSet1.add(2);
    subSet1.add(3);
    
    Set<Integer> subSet2 = new HashSet<Integer>();
    subSet2.add(10);
    subSet2.add(20);
    subSet2.add(30);
    
    Set<Collection<?>> mainSet = new HashSet<Collection<?>>();
    mainSet.add(subSet1);
    mainSet.add(subSet2);
    
    assertEquals(2, mainSet.size());
  }

}
