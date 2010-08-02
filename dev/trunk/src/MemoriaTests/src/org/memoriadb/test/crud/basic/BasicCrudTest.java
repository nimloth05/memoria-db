/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.test.crud.basic;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.crud.testclass.*;
import org.memoriadb.test.testclasses.*;
import org.memoriadb.test.testclasses.ctor.NoDefault;
import org.memoriadb.test.testclasses.weakref.WeakOwner;
import org.memoriadb.testutil.AbstractMemoriaTest;

public abstract class BasicCrudTest extends AbstractMemoriaTest {

  public void test_as_string() {
    IObjectId id = save(new Object());
    String asString = id.asString();
    IObjectId fromString = fObjectStore.getIdFactory().fromString(asString);
    assertEquals(id, fromString);
  }

  public void test_cannot_save_an_object_with_no_default_constructor() {
    try {
      fObjectStore.save(new NoDefault("1"));
      fail("InstantiationCheckException expected");
    } catch (SchemaException e) {
      //passed;
    }
  }

  public void test_change_int_value_and_integer_value_in_dataMode() {
    IntIntegerObject object = new IntIntegerObject(1, new Integer(2));
    IObjectId id = save(object);
    
    reopenDataMode();
    
    IFieldbasedObject l1_object = fDataStore.get(id);
    l1_object.set("fInt", 3);
    l1_object.set("fInteger", fDataStore.getRefactorApi().getLangValueObject(4));
    
    fDataStore.save(l1_object);
    
    reopen();
    
    assertEquals(3, ((IntIntegerObject)get(id)).getInt());
    assertEquals(new Integer(4), ((IntIntegerObject)get(id)).getInteger());
  }
  
  public void test_change_int_value_in_dataMode() {
    IntObject object = new IntObject(1);
    IObjectId id = save(object);
    
    reopenDataMode();
    
    IFieldbasedObject l1_object = fDataStore.get(id);
    l1_object.set("fInt", 2);
    
    fDataStore.save(l1_object);
    
    reopen();
    
    assertEquals(2, ((IntObject)get(id)).getInt());
  }
  
  public void test_change_int_value_with_LanguageValueObject() {
    IntObject object = new IntObject(1);
    IObjectId id = save(object);
    
    reopenDataMode();
    
    IFieldbasedObject l1_object = fDataStore.get(id);
    l1_object.set("fInt", fDataStore.getRefactorApi().getLangValueObject(2));
    
    try {
      fDataStore.save(l1_object);
      fail("Should throw a ClassCastException because the LangValueObject can not be cast to the Integer which the TypeEnum expects");
    }
    catch(MemoriaException e) {
      //passed
    }
  }

  public void test_cyclic_reference() {
    Cyclic1 c1 = new Cyclic1("c1");
    Cyclic2 c2 = new Cyclic2("c2");

    c1.setC2(c2);
    c2.setC1(c1);

    IObjectId idc1 = saveAll(c1);

    reopen();

    Cyclic1 c1_l1 = (Cyclic1) fObjectStore.get(idc1);
    Cyclic2 c2_l1 = c1_l1.getC2();

    assertNotSame(c1, c1_l1);
    assertEquals(c1, c1_l1);

    assertNotSame(c2, c2_l1);
    assertEquals(c2, c2_l1);

    assertEquals(c1_l1, c2_l1.getC1());

    // replace c2 by c3
    Cyclic2 c3 = new Cyclic2("c3");
    c1_l1.setC2(c3);
    c3.setC1(c1_l1);

    saveAll(c1_l1);

    reopen();

    Cyclic1 c1_l2 = (Cyclic1) fObjectStore.get(idc1);
    Cyclic2 c3_l1 = c1_l2.getC2();

    assertNotSame(c3, c3_l1);
    assertEquals(c3, c3_l1);
    assertEquals(c1_l2, c3.getC1());
  }

  public void test_get_memoria_class_data_mode() {
    IObjectId id = save(new StringObject("1"));
    reopenDataMode();

    IDataObject l1_obj = fDataStore.get(id);
    IObjectId memoriaClassId = fDataStore.getTypeInfo().getMemoriaClassId(l1_obj);
    assertNotNull(memoriaClassId);
  }

  public void test_getId() {
    Object o = new Object();
    assertNull(fObjectStore.getId(o));

    IObjectId id = save(o);

    assertEquals(o, fObjectStore.get(id));
  }

  public void test_Object() {
    Object o = new Object();
    IObjectId id = save(o);

    reopen();

    Object o_l1 = fObjectStore.get(id);
    assertSame(Object.class, o_l1.getClass());
  }

  public void test_objectId() {
    ObjectIdOwner ref = new ObjectIdOwner();
    IObjectId objId = save(new Object());
    ref.setObjectId(objId);
    IObjectId refId = save(ref);

    reopen();

    ObjectIdOwner l1_ref = fObjectStore.get(refId);

    assertEquals(objId, l1_ref.getObjectId());
  }

  public void test_objectId_is_saved_inline() {
    ObjectIdOwner ref1 = new ObjectIdOwner();
    IObjectId objId = save(new Object());
    ref1.setObjectId(objId);

    ObjectIdOwner ref2 = new ObjectIdOwner();
    ref2.setObjectId(ref1.getObjectId());

    // both ObjectIdOwners reference the same ObjectId
    assertSame(ref1.getObjectId(), ref2.getObjectId());

    IObjectId id1 = save(ref1);
    IObjectId id2 = save(ref2);

    reopen();

    ref1 = get(id1);
    ref2 = get(id2);

    assertNotSame(ref1.getObjectId(), ref2.getObjectId());
  }

  public void test_save_aggregate_with_dataObject_in_objectMode() {
    ObjectReferencer ref = new ObjectReferencer();
    ref.setObject(new DataObjectStub());
    try {
      saveAll(ref);
      fail("It's not possible to save a dataObject");
    }
    catch (MemoriaException e) {}
  }
  
  public void test_save_aggregated_boolean_primitive() {
    test_aggregated_primitive(Boolean.FALSE);
  }
  
  public void test_save_aggregated_byte_primitive() {
    test_aggregated_primitive((byte)1);
  }
  
  public void test_save_aggregated_character_primitive() {
    test_aggregated_primitive('c');
  }
  
  public void test_save_aggregated_double_primitive() {
    test_aggregated_primitive((double)1);
  }
  
  public void test_save_aggregated_float_primitive() {
    test_aggregated_primitive((float)1);
  }
  
  public void test_save_aggregated_integer_primitive() {
    test_aggregated_primitive(new Integer(1));
  }
  
  public void test_save_aggregated_long_primitive() {
    test_aggregated_primitive((long)1);
  }
  
  public void test_save_aggregated_short_primitive() {
    test_aggregated_primitive((short)1);
  }
  
  public void test_save_aggregated_string_primitive() {
    test_aggregated_primitive("Primitive!");
  }

  public void test_save_attribute() {
    StringObject object = new StringObject("object");
    IObjectId id = save(object);

    reopen();

    StringObject b_l1 = get(id);
    assertNotSame(b_l1, object);
    assertEquals("object", b_l1.getString());
  }

  public void test_save_cyclic_save_all() {
    Cyclic1 c1 = new Cyclic1("c1");
    Cyclic2 c2 = new Cyclic2("c2");

    c1.setC2(c2);
    c2.setC1(c1);

    IObjectId c1_objectId = saveAll(c1);

    reopen();

    Cyclic1 l1_c1 = fObjectStore.get(c1_objectId);
    Cyclic2 l1_c2 = fObjectStore.query(Cyclic2.class).get(0);

    assertSame(l1_c1.getC2(), l1_c2);
    assertSame(l1_c2.getC1(), l1_c1);
  }

  public void test_save_dataObject_in_objectMode() {
    try {
      save((Object)new DataObjectStub());
      fail("It's not possible to save a dataObject");
    }
    catch (MemoriaException e) {}
  }
  
  public void test_save_null_attribute() {
    StringObject object = new StringObject(null);
    IObjectId id = save(object);

    reopen();

    StringObject l1_b = get(id);
    assertNull(l1_b.getString());
  }

  public void test_save_null_reference() {
    A a = new A();

    save(a);
    delete(a);
    IObjectId id =saveAll(a);

    reopen();

    A l1_a = (A) fObjectStore.get(id);
    assertNull("b", l1_a.getB());
  }

  public void test_save_object() {
    Object o = new Object();
    IObjectId id = save(o);

    reopen();

    Object o_l1 = fObjectStore.get(id);
    assertSame(Object.class, o_l1.getClass());
  }

  public void test_save_object_twice_with_save_all() {
    ObjectReferencer referencer = new ObjectReferencer();
    referencer.setObject(new StringObject());
    IObjectId id = saveAll(referencer);

    referencer.setObject(new StringObject());
    saveAll(referencer);

    saveAll(referencer);

    reopen();

    assertTrue(fObjectStore.containsId(id));
  }

  public void test_save_object_with_static_field() {
    StaticFieldObject obj = new StaticFieldObject();
    IObjectId id = save(obj);

    reopen();

    assertTrue(fObjectStore.containsId(id));
  }


  public void test_save_primitive_fails() {
    beginUpdate();

    try{
      save(1);
      fail("primitive was saved");
    }
    catch(MemoriaException e) {
    }

    try{
      save(new Integer(1));
      fail("primitive was saved");
    }
    catch(MemoriaException e) {
    }

    try{
      save("str");
      fail("primitive was saved");
    }
    catch(MemoriaException e) {}

  }

  public void test_save_reference() {
    StringObject b = new StringObject("b");
    A a = new A(b);
    IObjectId id = saveAll(a);

    reopen();

    A a_l1 = (A) fObjectStore.get(id);
    assertEquals("b", a_l1.getB().getString());
  }

  public void test_save_same_object_twice() {
    StringObject object = new StringObject("object");
    IObjectId id = save(object);
    save(object);

    reopen();

    StringObject b_l1 = get(id);
    assertEquals("object", b_l1.getString());
  }

  public void test_save_single_object() {
    Object o = new StringObject("1");
    IObjectId id = save(o);

    reopen();

    Object o_l1 = fObjectStore.get(id);
    assertSame(StringObject.class, o_l1.getClass());
  }

  public void test_save_unsaved_reference() {
    A a = new A(null); // b is not saved
    IObjectId id = fObjectStore.save(a);

    reopen();

    A l1_a = fObjectStore.get(id);
    assertNull(l1_a.getB());
  }


  public void test_self_reference() {
    SelfReference obj = new SelfReference();
    IObjectId id = save(obj);

    reopen();

    SelfReference l1_obj = fObjectStore.get(id);
    l1_obj.assertRef();

    fObjectStore.delete(fObjectStore.get(id));

    reopen();

    assertFalse(fObjectStore.containsId(id));
  }

  public void test_self_reference_with_all_functions() {
    SelfReference obj = new SelfReference();
    IObjectId id = saveAll(obj);

    reopen();

    SelfReference l1_obj = fObjectStore.get(id);
    l1_obj.assertRef();

    fObjectStore.deleteAll(l1_obj);

    reopen();

    assertFalse(fObjectStore.containsId(id));
  }

  public void test_transient_ref_with_save_and_saveAll() {
    TransientClass t1 = new TransientClass(1, new A());
    TransientClass t2 = new TransientClass(2, new A());

    IObjectId id1 = save(t1);
    IObjectId id2 = save(t2);

    reopen();

    TransientClass l1_t1 = get(id1);
    TransientClass l1_t2 = get(id2);

    assertEquals(0, l1_t1.fInt);
    assertNull(l1_t1.fA);

    assertEquals(0, l1_t2.fInt);
    assertNull(l1_t2.fA);
  }

  public void test_update_attribute() {
    StringObject object = new StringObject("1");
    IObjectId id1 = save(object);
    object.setString("2");
    IObjectId id2 = save(object);
    assertEquals(id1, id2);

    reopen();

    StringObject l1_b = get(id2);
    assertEquals("2", l1_b.getString());
  }

  public void test_update_unsaved_reference() {
    A a = new A(null); // b is not saved
    IObjectId id = fObjectStore.save(a);

    reopen();

    A l1_a = fObjectStore.get(id);
    assertNull(l1_a.getB());

    StringObject b = new StringObject();
    fObjectStore.beginUpdate();
    a.setB(b);
    fObjectStore.save(b);

    fObjectStore.endUpdate();

    A l2_a = fObjectStore.get(id);
    assertNull(l2_a.getB());

    StringObject l2_b = fObjectStore.query(StringObject.class).get(0);
    l2_a.setB(l2_b);
    fObjectStore.save(l2_a);
    reopen();

    A l3_a = fObjectStore.get(id);
    assertNotNull(l3_a.getB());
  }

  public void test_weak_ref_deleteAll() {
    Object obj = new Object();
    WeakOwner ref = new WeakOwner(obj);

    IObjectId objId = save(obj);
    IObjectId refId = saveAll(ref);

    deleteAll(ref);

    reopen();

    assertFalse(fObjectStore.containsId(refId));
    assertTrue(fObjectStore.containsId(objId));
  }

  public void test_weak_ref_saveAll() {
    Object obj = new Object();
    WeakOwner ref = new WeakOwner(obj);

    try{
      saveAll(ref);
      fail("because the reference in WeakOwner is annotated with @weakRef, the aggregated object should not be saved.");
    }
    catch(MemoriaException e) {
      // pass
    }

  }

  public void test_weak_ref_saveAll_stops_traversing() {
    Object obj = new Object();
    WeakOwner ref = new WeakOwner(obj);

    save(obj);
    long expectedRevision = fObjectStore.getObjectInfo(obj).getRevision();

    saveAll(ref);

    assertEquals(expectedRevision, fObjectStore.getObjectInfo(obj).getRevision());
  }

  private void test_aggregated_primitive(Object primitive) {
    ObjectReferencer object = new ObjectReferencer(primitive);
    IObjectId id = saveAll(object);
    
    reopen();
    
    ObjectReferencer object_l1 = get(id);
    assertEquals(primitive, object_l1.getObject());
  }

}
