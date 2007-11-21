package org.memoriadb.test.core.handler.list;

import java.util.List;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.list.IListDataObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public abstract class ListTest extends AbstractObjectStoreTest {
  
  private DBMode fDbMode = DBMode.clazz;

  public void test_data_mode() {
    List<SimpleTestObj> objectList = getObjectList();
    IObjectId objectId = saveAll(objectList);
    
    fDbMode = DBMode.data;
    reopen();
    
    IListDataObject l1_list = fStore.getObject(objectId);
    assertEquals(objectList.size(), l1_list.getList().size());
    l1_list.getList().remove(0);
    save(l1_list);
    
    fDbMode = DBMode.clazz;
    reopen();
    
    List<SimpleTestObj> l2_list = fStore.getObject(objectId);
    assertEquals(objectList.size(), l2_list.size() + 1);
    assertEquals(objectList.get(1), l2_list.get(0));
  }

  public void test_data_mode_scenario() {
    List<SimpleTestObj> objectList = getObjectList();
    IObjectId objectId = saveAll(objectList);
    
    fDbMode = DBMode.data;
    reopen();
    
    IListDataObject l1_list = fStore.getObject(objectId);
    assertEquals(objectList.size(), l1_list.getList().size());
    Object object = l1_list.getList().get(0);
    
    l1_list.getList().clear();
    
    l1_list.getList().add(object);
    Object newObj = SimpleTestObj.createFieldObject(fStore, "newObj");
    l1_list.getList().add(newObj);
    
    save(newObj);
    save(l1_list);
    
    fDbMode = DBMode.clazz;
    reopen();
    
    List<SimpleTestObj> l2_list = fStore.getObject(objectId);
    assertEquals(objectList.size(), l2_list.size());
    assertEquals("newObj", l2_list.get(1).getString());
  }

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
  
  @Override
  protected void configureReopen(CreateConfig config) {
    super.configureReopen(config);
    config.setDBMode(fDbMode );
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
