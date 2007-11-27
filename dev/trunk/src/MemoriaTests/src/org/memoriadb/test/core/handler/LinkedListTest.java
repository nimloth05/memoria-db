package org.memoriadb.test.core.handler;

import java.util.LinkedList;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class LinkedListTest extends AbstractMemoriaTest {
  
  public void test_int() {
    LinkedList<Integer> list = new LinkedList<Integer>();
    list.add(1);
    list.add(1);
    
    IObjectId id = save(list);
    
    reopen();
     
    LinkedList<String> list_l1 = get(id);
    
    assertEquals(list, list_l1);
    System.out.println(list_l1.size());
  }

  public void test_object() {
    LinkedList<SimpleTestObj> list = new LinkedList<SimpleTestObj>();
    list.add(new SimpleTestObj("one"));
    list.add(new SimpleTestObj("two"));
    
    IObjectId id = saveAll(list);
    
    reopen();
     
    LinkedList<String> list_l1 = get(id);
    
    assertEquals(list, list_l1);
  }
  

  public void test_string() {
    LinkedList<String> list = new LinkedList<String>();
    list.add("one");
    list.add("two");
    
    IObjectId id = saveAll(list);
    
    reopen();
     
    LinkedList<String> list_l1 = get(id);
    
    assertEquals(list, list_l1);
  }
  
}
