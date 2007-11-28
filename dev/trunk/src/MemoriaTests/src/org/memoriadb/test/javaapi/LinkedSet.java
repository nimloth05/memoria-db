package org.memoriadb.test.javaapi;

import java.util.*;

import junit.framework.TestCase;

public class LinkedSet extends TestCase {
  public void test_linked_HashSet() {
    Set<Integer> s1 = new LinkedHashSet<Integer>();
    s1.add(1);
    s1.add(2);

    Set<Integer> s2 = new LinkedHashSet<Integer>();
    s2.add(1);
    s2.add(2);

    Set<Integer> s3 = new LinkedHashSet<Integer>();
    s3.add(2);
    s3.add(1);
    
    assertTrue(s1.equals(s2));
    
    // WRONG!
    assertTrue(s1.equals(s3));
    
  }
}
