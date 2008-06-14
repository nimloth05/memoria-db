package org.memoriadb.test.core;

import org.memoriadb.handler.field.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class ModeTest extends AbstractMemoriaTest {

  public void test_add_object() {
    SimpleTestObj obj = new SimpleTestObj("1");
    IObjectId id1 = save(obj);
    
    IObjectId memoriaClassId = fObjectStore.getTypeInfo().getMemoriaClassId(obj);
    
    reopenDataMode();
    
    IFieldbasedObject obj2 = new FieldbasedDataObject(memoriaClassId);
    obj2.set("fString", "2");
    IObjectId id2 = save(obj2);
    
    reopen();
    
    assertEquals(2, query(SimpleTestObj.class).size());
    
    obj = get(id1);
    assertEquals("1", obj.getString());

    obj = get(id2);
    assertEquals("2", obj.getString());

  }
  
  public void test_delete_object_in_data_mode() {
    SimpleTestObj obj = new SimpleTestObj("1");
    IObjectId id = save(obj);
    
    reopenDataMode();
    
    fDataStore.delete(fDataStore.get(id));
    
    reopen();
    
    assertFalse(fObjectStore.containsId(id));
  }

  public void test_save_obj() {
    SimpleTestObj obj = new SimpleTestObj("1");
    IObjectId id = save(obj);
    
    reopenDataMode();
    
    //We get a special object, because we are in the data mode
    IFieldbasedObject l1_obj = fDataStore.get(id);
    assertEquals(obj.getString(), l1_obj.get("fString"));
  }
  
  public void test_update_obj() {
    SimpleTestObj obj = new SimpleTestObj("1");
    IObjectId id = save(obj);
    
    reopenDataMode();
    
    //We get a special object, because we are in the data mode
    IFieldbasedObject l1_obj = fDataStore.get(id);
    l1_obj.set("fString", "2");
    save(l1_obj);
    
    reopen();
    
    SimpleTestObj l2_obj = fObjectStore.get(id);
    assertEquals(l1_obj.get("fString"), l2_obj.getString());
  }
}
