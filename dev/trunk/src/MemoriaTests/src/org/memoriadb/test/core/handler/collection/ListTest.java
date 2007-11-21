package org.memoriadb.test.core.handler.collection;

import java.util.List;

import org.memoriadb.core.DBMode;
import org.memoriadb.core.handler.list.IListDataObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;

public abstract class ListTest extends CollectionTest {
  
  public void test_data_mode() {
    List<SimpleTestObj> objectList = (List<SimpleTestObj>) getObjectCollection();
    IObjectId objectId = saveAll(objectList);
    
    reopen(DBMode.data);
    
    IListDataObject l1_list = fStore.getObject(objectId);
    assertEquals(objectList.size(), l1_list.getList().size());
    l1_list.getList().remove(0);
    save(l1_list);
    
    reopen(DBMode.clazz);
    
    List<SimpleTestObj> l2_list = fStore.getObject(objectId);
    assertEquals(objectList.size(), l2_list.size() + 1);
    assertEquals(objectList.get(1), l2_list.get(0));
  }

  public void test_data_mode_scenario() {
    List<SimpleTestObj> objectList = (List<SimpleTestObj>) getObjectCollection();
    IObjectId objectId = saveAll(objectList);
    
    reopen(DBMode.data);
    
    IListDataObject l1_list = fStore.getObject(objectId);
    assertEquals(objectList.size(), l1_list.getList().size());
    Object object = l1_list.getList().get(0);
    
    l1_list.getList().clear();
    
    l1_list.getList().add(object);
    Object newObj = SimpleTestObj.createFieldObject(fStore, "newObj");
    l1_list.getList().add(newObj);
    
    save(newObj);
    save(l1_list);
    
    reopen(DBMode.clazz);
    
    List<SimpleTestObj> l2_list = fStore.getObject(objectId);
    assertEquals(objectList.size(), l2_list.size());
    assertEquals("newObj", l2_list.get(1).getString());
  }

}
