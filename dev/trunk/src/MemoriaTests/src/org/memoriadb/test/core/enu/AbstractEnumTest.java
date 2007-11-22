package org.memoriadb.test.core.enu;

import org.memoriadb.core.handler.def.field.IFieldObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.enums.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public abstract class AbstractEnumTest extends AbstractObjectStoreTest {
  
  public void test_data_mode() {
    EnumUse obj = new EnumUse();
    obj.setEnum(TestEnum.b);
    IObjectId objId = save(obj);
    obj = null;
    
    reopen(DBMode.data);
    
    IFieldObject l1_obj = fStore.getObject(objId);
    assertEquals(TestEnum.b.ordinal(), l1_obj.get("fEnum"));
  }
  
  public void test_field() {
    EnumUse obj = new EnumUse();
    obj.setEnum(TestEnum.b);
    IObjectId objId = save(obj);
    obj = null;
    
    reopen();
    EnumUse l1_obj = fStore.getObject(objId);
    assertEquals(TestEnum.b, l1_obj.getEnum());
  }
  
  public void test_null_ref() {
    EnumUse obj = new EnumUse();
    obj.setEnum(null);
    IObjectId objId = save(obj);
    obj = null;
    
    reopen();
    EnumUse l1_obj = fStore.getObject(objId);
    assertEquals(null, l1_obj.getEnum());
  }
  
  public void test_scenario() {
    EnumUse obj = new EnumUse();
    obj.setEnum(null);
    IObjectId objId = save(obj);
    obj = null;
    
    reopen();
    EnumUse l1_obj = fStore.getObject(objId);
    assertEquals(null, l1_obj.getEnum());
    
    l1_obj.setEnum(TestEnum.a);
    save(l1_obj);
    
    reopen();
    EnumUse l2_obj = fStore.getObject(objId);
    assertEquals(l1_obj.getEnum(), l2_obj.getEnum());
    
    l2_obj.setEnum(TestEnum.b);
    save(l2_obj);
    
    reopen();
    EnumUse l3_obj = fStore.getObject(objId);
    assertEquals(l2_obj.getEnum(), l3_obj.getEnum());
  }


}
