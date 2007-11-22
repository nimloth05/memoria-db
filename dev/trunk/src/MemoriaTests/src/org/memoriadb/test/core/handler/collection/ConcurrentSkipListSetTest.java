package org.memoriadb.test.core.handler.collection;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConcurrentSkipListSetTest extends SetTest {
  
  @Override
  public void test_list_in_list() {
    try {
      super.test_list_in_list();
      fail("Its not possible to add a TreeSet to a TreeSet beacuse it does not implement the Comparable");
    } 
    catch (ClassCastException e) {
      //Its not possible to add a TreeSet to a TreeSet beacuse it does not implement the Comparable
    }
  }
  
  @Override
  public void test_mixed_list() {
    try {
      super.test_list_in_list();
      fail("Its not possible to add a TreeSet to a TreeSet beacuse it does not implement the Comparable");
    } 
    catch (ClassCastException e) {
      //Its not possible to add a TreeSet to a TreeSet beacuse it does not implement the Comparable
    }
  }

  @Override
  protected <T> Collection<T> createCollection() {
    return new ConcurrentSkipListSet<T>();
  }

}
