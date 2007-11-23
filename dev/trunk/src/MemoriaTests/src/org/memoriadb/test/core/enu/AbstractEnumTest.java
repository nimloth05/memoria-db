package org.memoriadb.test.core.enu;

import java.util.*;

import org.memoriadb.core.handler.enu.IEnumObject;
import org.memoriadb.core.handler.field.IFieldObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.enums.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public abstract class AbstractEnumTest extends AbstractObjectStoreTest {
  
  public void test_data_mode() {
    EnumUse obj = new EnumUse();
    obj.setEnum(TestEnum.b);
    IObjectId objId = saveAll(obj);
    obj = null;
    
    reopenDataMode();
    
    IFieldObject l1_obj = fDataStore.getObject(objId);
    IEnumObject l1_enumObjet = (IEnumObject) l1_obj.get("fEnum");
    assertEquals(TestEnum.b.ordinal(), l1_enumObjet.getOrdinal());
  }
  
  public void test_enum_set() {
    EnumSet<TestEnum> enumSet = EnumSet.allOf(TestEnum.class);
    IObjectId id = saveAll(enumSet);
    
    reopen();
    EnumSet<TestEnum> l1_enumSet =  get(id);
    assertEquals(2, l1_enumSet.size());
    
    l1_enumSet.remove(TestEnum.a);
    save(l1_enumSet);
    
    reopen();
    
    EnumSet<TestEnum> l2_enumSet = get(id);
    assertEquals(1, l2_enumSet.size());
  }
  
  public void test_field() {
    EnumUse obj = new EnumUse();
    obj.setEnum(TestEnum.b);
    IObjectId objId = saveAll(obj);
    obj = null;
    
    reopen();
    EnumUse l1_obj = fObjectStore.getObject(objId);
    assertEquals(TestEnum.b, l1_obj.getEnum());
  }
  
  public void test_null_ref() {
    EnumUse obj = new EnumUse();
    obj.setEnum(null);
    IObjectId objId = save(obj);
    obj = null;
    
    reopen();
    EnumUse l1_obj = fObjectStore.getObject(objId);
    assertEquals(null, l1_obj.getEnum());
  }
  
  public void test_scenario() {
    EnumUse obj = new EnumUse();
    obj.setEnum(null);
    IObjectId objId = save(obj);
    obj = null;
    
    reopen();
    EnumUse l1_obj = fObjectStore.getObject(objId);
    assertEquals(null, l1_obj.getEnum());
    
    l1_obj.setEnum(TestEnum.a);
    saveAll(l1_obj);
    
    reopen();
    EnumUse l2_obj = fObjectStore.getObject(objId);
    assertEquals(l1_obj.getEnum(), l2_obj.getEnum());
    
    l2_obj.setEnum(TestEnum.b);
    saveAll(l2_obj);
    
    reopen();
    EnumUse l3_obj = fObjectStore.getObject(objId);
    assertEquals(l2_obj.getEnum(), l3_obj.getEnum());
  }
  
  public void test_use_enum_in_a_object_field() {
    ObjectEnumUse obj = new ObjectEnumUse();
    obj.setEnum(TestEnum.b);
    IObjectId id = saveAll(obj);
    
    reopen();
    
    ObjectEnumUse l1_obj = get(id);
    assertEquals(TestEnum.b, l1_obj.getEnum());
  }
  
  public void test_use_enum_list() {
    List<Object> list = new LinkedList<Object>();
    list.add(TestEnum.a);
    IObjectId id = saveAll(list);
    
    reopen();
    List<Object> l1_list = get(id);
    assertEquals(1, fObjectStore.getAll(TestEnum.class).size());
    assertFalse(l1_list.isEmpty());
    assertEquals(TestEnum.a, l1_list.get(0));
  }


}
