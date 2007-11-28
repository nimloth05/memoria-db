package org.memoriadb.test.core.crud.basic;

import org.memoriadb.core.exception.*;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.core.crud.testclass.*;
import org.memoriadb.test.core.testclasses.*;
import org.memoriadb.test.core.testclasses.ctor.NoDefault;
import org.memoriadb.testutil.AbstractMemoriaTest;

public abstract class BasicCrudTest extends AbstractMemoriaTest {

  public void test_as_string() {
    IObjectId id = save(new Object());
    String asString = id.asString();
    IObjectId fromString = fObjectStore.getIdFactory().fromString(asString);
    assertEquals(id, fromString);
  }
  
  public void test_cannot_save_an_object_with_no_default_ctor() {
    try {
      fObjectStore.save(new NoDefault("1"));
      fail("InstantiationCheckException expected");
    } catch (SchemaException e) {
      //passed;
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
    IObjectId id = save(new SimpleTestObj("1"));
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
  
  public void test_ObjectId() {
    Object obj = new Object();
    IObjectId objId = save(obj);
    IObjectId idId = saveAll(objId);
    
    reopen();
    
    IObjectId l1_objId = get(idId);
    
    assertEquals(objId, l1_objId);
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
  
  public void test_save_attribute() {
    B b = new B("b");
    IObjectId id = fObjectStore.save(b);
    
    reopen();
    
    B b_l1 = (B) fObjectStore.get(id);
    assertNotSame(b_l1, b);
    assertEquals("b", b_l1.getName());
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
    B b = new B(null);
    IObjectId id = fObjectStore.save(b);
    
    reopen();
    
    B l1_b = (B) fObjectStore.get(id);
    assertNull(l1_b.getName());
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
    catch(MemoriaException e) {
    }
    
  }
  
  public void test_save_reference() {
    B b = new B("b");
    A a = new A(b);
    IObjectId id = saveAll(a);
    
    reopen();
    
    A a_l1 = (A) fObjectStore.get(id);
    assertEquals("b", a_l1.getB().getName());
  }
  
  public void test_save_same_object_twice() {
    B b = new B("b");
    IObjectId id = save(b);
    save(b);
    
    reopen();
    
    B b_l1 = (B) fObjectStore.get(id);
    assertEquals("b", b_l1.getName());
  }
  
  public void test_save_single_object() {
    Object o = new SimpleTestObj("1");
    IObjectId id = save(o);
    
    reopen();
    
    Object o_l1 = fObjectStore.get(id);
    assertSame(SimpleTestObj.class, o_l1.getClass());
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
    B b = new B("b");
    IObjectId idb = fObjectStore.save(b);
    b.setName("bb");
    IObjectId idbb = fObjectStore.save(b);
    assertEquals(idb, idbb);
    
    reopen();
    
    B b_l1 = (B) fObjectStore.get(idbb);
    assertEquals("bb", b_l1.getName());
  }
  
  public void test_update_unsaved_reference() {
    A a = new A(null); // b is not saved
    IObjectId id = fObjectStore.save(a);
    
    reopen();
    
    A l1_a = fObjectStore.get(id);
    assertNull(l1_a.getB());
    
    B b = new B();
    fObjectStore.beginUpdate();
    a.setB(b);
    fObjectStore.save(b);
    
    fObjectStore.endUpdate();
    
    A l2_a = fObjectStore.get(id);
    assertNull(l2_a.getB());

    B l2_b = fObjectStore.query(B.class).get(0);
    l2_a.setB(l2_b);
    fObjectStore.save(l2_a);
    reopen();
    
    A l3_a = fObjectStore.get(id);
    assertNotNull(l3_a.getB());
  }

  
}
