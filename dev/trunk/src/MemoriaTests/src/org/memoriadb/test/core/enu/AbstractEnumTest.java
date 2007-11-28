package org.memoriadb.test.core.enu;

import java.util.*;

import org.memoriadb.handler.enu.IEnumObject;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.enums.*;
import org.memoriadb.testutil.*;
import org.memoriadb.testutil.Collections;

public abstract class AbstractEnumTest extends AbstractMemoriaTest {
  
  public void test_add_enum_in_data_mode() {
    
    save(TestEnum.c);
    
    reopenDataMode();
    
    fDataStore.save(fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), 2));
    
    reopen();
  }
  
  public void test_data_mode() {
    EnumUse obj = new EnumUse();
    obj.setEnum(TestEnum.b);
    IObjectId objId = saveAll(obj);
    obj = null;
    
    reopenDataMode();
    
    IFieldbasedObject l1_obj = fDataStore.get(objId);
    IEnumObject l1_enumObjet = (IEnumObject) l1_obj.get("fEnum");
    assertEquals(TestEnum.b.ordinal(), l1_enumObjet.getOrdinal());
  }
  
  public void test_derived_enum() {
    EnumUse obj = new EnumUse();
    obj.setEnum(TestEnum.c);
    IObjectId id = saveAll(obj);
    
    reopen();
    
    EnumUse l1_obj = get(id);
    assertEquals(TestEnum.c, l1_obj.getEnum());
  }
  
  public void test_enum_array() {
    TestEnum[] enumArray = new TestEnum[2];
    enumArray[0] = TestEnum.a;
    enumArray[1] = TestEnum.c;
    IObjectId id = saveAll(enumArray);
    
    reopen();
    TestEnum[] l1_enumArray = get(id);
    assertTrue(Arrays.deepEquals(enumArray, l1_enumArray));
  }
  
  public void test_enum_set() {
    EnumSet<TestEnum> enumSet = EnumSet.noneOf(TestEnum.class);
    enumSet.add(TestEnum.a);
    enumSet.add(TestEnum.b);
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
    EnumUse l1_obj = fObjectStore.get(objId);
    assertEquals(TestEnum.b, l1_obj.getEnum());
  }
  
  public void test_null_ref() {
    EnumUse obj = new EnumUse();
    obj.setEnum(null);
    IObjectId objId = save(obj);
    obj = null;
    
    reopen();
    EnumUse l1_obj = fObjectStore.get(objId);
    assertEquals(null, l1_obj.getEnum());
  }
  
  public void test_save_derived_enum() {
    IObjectId id = save(TestEnum.c);
    reopen();
    TestEnum l1_enum = get(id);
    assertEquals(TestEnum.c, l1_enum);
  }
  
  public void test_save_enum() {
    IObjectId id = save(TestEnum.a);
    reopen();
    TestEnum l1_enum = get(id);
    assertEquals(TestEnum.a, l1_enum);
  }
  
  public void test_save_enum_twice() {
    save(TestEnum.a);
    save(TestEnum.a);
    save(TestEnum.c);
    save(TestEnum.c);
    
    reopen();
    
    Collections.containsAll(fObjectStore.query(TestEnum.class), TestEnum.a, TestEnum.c);
  }
  
  public void test_scenario() {
    EnumUse obj = new EnumUse();
    obj.setEnum(null);
    IObjectId objId = save(obj);
    obj = null;
    
    reopen();
    EnumUse l1_obj = fObjectStore.get(objId);
    assertEquals(null, l1_obj.getEnum());
    
    l1_obj.setEnum(TestEnum.a);
    saveAll(l1_obj);
    
    reopen();
    EnumUse l2_obj = fObjectStore.get(objId);
    assertEquals(l1_obj.getEnum(), l2_obj.getEnum());
    
    l2_obj.setEnum(TestEnum.b);
    saveAll(l2_obj);
    
    reopen();
    EnumUse l3_obj = fObjectStore.get(objId);
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
    assertEquals(1, fObjectStore.query(TestEnum.class).size());
    assertFalse(l1_list.isEmpty());
    assertEquals(TestEnum.a, l1_list.get(0));
  }


}
