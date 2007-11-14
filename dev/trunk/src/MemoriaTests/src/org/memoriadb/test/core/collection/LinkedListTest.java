package org.memoriadb.test.core.collection;

import java.util.LinkedList;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class LinkedListTest extends AbstractObjectStoreTest {
  
  public void test_int() {
    LinkedList<Integer> list = new LinkedList<Integer>();
    list.add(1);
    list.add(1);
    
    IObjectId id = save(list);
    
    reopen();
     
    LinkedList<String> list_l1 = get(id);
    
    assertEquals(list, list_l1);
  }

  public void test_string() {
    LinkedList<String> list = new LinkedList<String>();
    list.add("one");
    list.add("two");
    
    IObjectId id = save(list);
    
    reopen();
     
    LinkedList<String> list_l1 = get(id);
    
    assertEquals(list, list_l1);
  }
  
}
