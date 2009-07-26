package org.memoriadb.test.handler.collection;

import org.memoriadb.test.testclasses.StringObject;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConcurrentSkipListSetTest extends SetTest {
  
  @Override
  public void test_collection_with_objectIds() {
    try {
      super.test_collection_with_objectIds();
      fail("The IObjectId is not comparble.");
    }
    catch (ClassCastException e) {
      
    }
  }
  
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
  public void test_list_in_list_in_multiple_save_calls() {
    try {
      super.test_list_in_list_in_multiple_save_calls();
      fail("Its not possible to add a TreeSet to a TreeSet beacuse it does not implement the Comparable");
    } 
    catch (ClassCastException e) {
      //Its not possible to add a TreeSet to a TreeSet beacuse it does not implement the Comparable
    }
  }
  
  
  
  @Override
  public void test_mixed_list() {
    try {
      Collection<Object> collection = createCollection();
      collection.add(new StringObject("1"));
      collection.add(1);
      fail("It's not possible to add multiple types to a ConcurrentSkipListSet");
    }
    catch (ClassCastException e) {
      
    }
  }
  
  @Override
  public void test_null_reference() {
    try {
      super.test_null_reference();
      fail("It's not possible to add null");
    } 
    catch (NullPointerException e) {
    }
  }

  @Override
  protected <T> Collection<T> createCollection() {
    return new ConcurrentSkipListSet<T>();
  }

}
