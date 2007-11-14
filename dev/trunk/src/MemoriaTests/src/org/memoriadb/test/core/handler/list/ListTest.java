package org.memoriadb.test.core.handler.list;

import java.util.List;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public abstract class ListTest extends AbstractObjectStoreTest {
  
  @SuppressWarnings("unchecked")
  public void test_empty_list() {
    List list = createList();
    reopen(list);
  }

  @SuppressWarnings("unchecked")
  public void test_int_object() {
    List list = getIntObjectList();
    reopen(list);
  }

  @SuppressWarnings("unchecked")
  public void test_int_primitive() {
    List list = getIntPrimitiveList();
    reopen(list);
  }

  @SuppressWarnings("unchecked")
  public void test_list_in_list() {
    List list = createList();
    list.add(getIntObjectList());
    list.add(getIntPrimitiveList());
    list.add(getObjectList());
    
    reopen(list);
  }
  
  @SuppressWarnings("unchecked")
  public void test_object() {
    List list = getObjectList();
    reopen(list);
  }

  public void test_string() {
    List list = createList();
    list.add("one");
    list.add("two");
    reopen(list);
  }
  
  protected abstract List createList();

  private List getIntObjectList() {
    List list = createList();
    list.add(new Integer(1));
    list.add(new Integer(2));
    return list;
  }
  
  private List getIntPrimitiveList() {
    List list = createList();
    list.add(1);
    list.add(1);
    return list;
  }
  
  private List getObjectList() {
    List list = createList();
    list.add(new SimpleTestObj("1"));
    list.add(new SimpleTestObj("2"));
    return list;
  }
  
  private void reopen(List list) {
    IObjectId id = saveAll(list);
    reopen();
    assertEquals(list, get(id));
  }
  
}
