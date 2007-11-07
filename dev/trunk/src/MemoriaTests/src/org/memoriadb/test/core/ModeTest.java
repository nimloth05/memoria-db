package org.memoriadb.test.core;

import org.memoriadb.core.DBMode;
import org.memoriadb.core.handler.def.field.IFieldObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class ModeTest extends AbstractObjectStoreTest {

  public void test_save_obj() {
    SimpleTestObj obj = new SimpleTestObj("1");
    IObjectId id = save(obj);
    
    reopen(DBMode.data);
    
    //We get a special object, because we are in the data mode
    IFieldObject l1_obj = fStore.getObject(id);
    assertEquals(obj.getString(), l1_obj.get("fString"));
  }
  
  public void test_update_obj() {
    SimpleTestObj obj = new SimpleTestObj("1");
    IObjectId id = save(obj);
    
    reopen(DBMode.data);
    
    //We get a special object, because we are in the data mode
    IFieldObject l1_obj = fStore.getObject(id);
    l1_obj.set("fString", "2");
    save(l1_obj);
    
    reopen(DBMode.clazz);
    
    SimpleTestObj l2_obj = fStore.getObject(id);
    assertEquals(l1_obj.get("fString"), l2_obj.getString());
  }
  
  
}
