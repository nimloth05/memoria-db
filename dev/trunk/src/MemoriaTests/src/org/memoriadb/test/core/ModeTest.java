package org.memoriadb.test.core;

import org.memoriadb.core.handler.field.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class ModeTest extends AbstractObjectStoreTest {

  public void test_add_obejct() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    IObjectId memoriaClassId = fObjectStore.typeInfo().getMemoriaClassId(obj);
    
    reopenDataMode();
    
    IFieldObject obj2 = new FieldMapDataObject(memoriaClassId);
    obj2.set("fString", 2);
    save(obj2);
    
    reopen();
    
    assertEquals(2, getAll(SimpleTestObj.class).size());
  }
  
  public void test_delete_object_in_data_mode() {
    SimpleTestObj obj = new SimpleTestObj("1");
    IObjectId id = save(obj);
    
    reopenDataMode();
    
    fDataStore.delete(fDataStore.getObject(id));
    
    reopen();
    
    assertFalse(fObjectStore.containsId(id));
  }

  public void test_save_obj() {
    SimpleTestObj obj = new SimpleTestObj("1");
    IObjectId id = save(obj);
    
    reopenDataMode();
    
    //We get a special object, because we are in the data mode
    IFieldObject l1_obj = fDataStore.getObject(id);
    assertEquals(obj.getString(), l1_obj.get("fString"));
  }
  
  public void test_update_obj() {
    SimpleTestObj obj = new SimpleTestObj("1");
    IObjectId id = save(obj);
    
    reopenDataMode();
    
    //We get a special object, because we are in the data mode
    IFieldObject l1_obj = fDataStore.getObject(id);
    l1_obj.set("fString", "2");
    save(l1_obj);
    
    reopen();
    
    SimpleTestObj l2_obj = fObjectStore.getObject(id);
    assertEquals(l1_obj.get("fString"), l2_obj.getString());
  }
}
