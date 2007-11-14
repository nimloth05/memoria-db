package org.memoriadb.test.core.handler.list;

import java.util.List;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public abstract class ListTest extends AbstractObjectStoreTest {
  
  public void test_empty_list() {
    List<Object> list = createList();
    reopen(list);
  }

  public void test_int_object() {
    List<Integer> list = getIntObjectList();
    reopen(list);
  }

  public void test_int_primitive() {
    List<Integer> list = getIntPrimitiveList();
    reopen(list);
  }

  public void test_list_in_list() {
    List<List<?>> list = createList();
    list.add(getIntObjectList());
    list.add(getIntPrimitiveList());
    list.add(getObjectList());
    
    reopen(list);
  }
  
  public void test_list_lifecycle() {
    List<SimpleTestObj> list = getObjectList();
    SimpleTestObj obj1 = list.get(0);
    reopen(list);
    
    list.clear();
    reopen(list);
    
    list.add(new SimpleTestObj("3"));
    reopen(list);
    
    list.add(obj1);
    reopen(list);
  }
  
  public void test_mixed_list() {
    List<Object> list = createList();
    list.add("1");
    list.add(getIntObjectList());
    list.add(new SimpleTestObj());
    
    reopen(list);
  }

  public void test_object() {
    List<SimpleTestObj> list = getObjectList();
    reopen(list);
  }
  
  public void test_string() {
    List<String> list = createList();
    list.add("one");
    list.add("two");
    reopen(list);
  }
  
  protected abstract <T> List<T> createList();

  private List<Integer> getIntObjectList() {
    List<Integer> list = createList();
    list.add(new Integer(1));
    list.add(new Integer(2));
    return list;
  }
  
  private List<Integer> getIntPrimitiveList() {
    List<Integer> list = createList();
    list.add(1);
    list.add(1);
    return list;
  }
  
  private List<SimpleTestObj> getObjectList() {
    List<SimpleTestObj> list = createList();
    list.add(new SimpleTestObj("1"));
    list.add(new SimpleTestObj("2"));
    return list;
  }
  
  private void reopen(List<?> list) {
    IObjectId id = saveAll(list);
    reopen();
    assertEquals(list, get(id));
  }
  
}
