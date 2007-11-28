package org.memoriadb.test.javaapi;

import java.util.*;

import junit.framework.TestCase;

public class SetTest extends TestCase {
  
  public void test_hashCode() {
    Set<Integer> set = new HashSet<Integer>();
    
    for(int i = 0; i < 10; ++i) {
      set.add(i*3);
    }
    System.out.println(set);
  }

}
