package org.memoriadb.test.core;

import java.util.*;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.exception.SchemaException;
import org.memoriadb.test.core.testclasses.*;
import org.memoriadb.testutil.*;
import org.memoriadb.testutil.Collections;
import org.memoriadb.util.Constants;

public class ObjectContainerTest extends AbstractObjectStoreTest {

  public void test_contains() {
    SimpleTestObj obj = new SimpleTestObj();
    IObjectId id = save(obj);
    
    assertTrue(fObjectStore.contains(obj));
    assertTrue(fObjectStore.containsId(id));
  }
  
  public void test_HeadRevision() {
    assertEquals(Constants.INITIAL_HEAD_REVISION+1, fObjectStore.getHeadRevision());
    save(new Object());
  }
  
  public void test_incorrect_hash_code_objects() {
    WrongHashCode obj1 = new WrongHashCode("1");
    WrongHashCode obj2 = new WrongHashCode("2");

    save(obj1);
    save(obj2);
    
    List<WrongHashCode> expectedObjs = Arrays.asList(obj1, obj2);
    reopen();

    List<WrongHashCode> actualObjs = query(WrongHashCode.class);

    Iterator<WrongHashCode> expectedIter = expectedObjs.iterator();
    while (expectedIter.hasNext()) {
      WrongHashCode savedObj = expectedIter.next();

      Iterator<WrongHashCode> actualIter = actualObjs.iterator();
      while (actualIter.hasNext()) {
        if (savedObj.fValue.equals(actualIter.next().fValue)) actualIter.remove();
      }
    }
    assertTrue("Not all objects where loaded/saved: " + actualObjs, actualObjs.isEmpty());
  }
  
  public void test_save_null_primitive_objects() {
    FieldTypeTestClass obj = new FieldTypeTestClass();
    save(obj);
    
    assertNull(obj.fBooleanC);
    assertNull(obj.fByteC);
    assertNull(obj.fCharC);
    assertNull(obj.fDoubleC);
    assertNull(obj.fFloatC);
    assertNull(obj.fIntC);
    assertNull(obj.fLongC);
    assertNull(obj.fShortC);
    assertNull(obj.fString);
  }
  
  public void test_save_object() {
    List<SimpleTestObj> objects = new ArrayList<SimpleTestObj>();
    fObjectStore.beginUpdate();
    for (int i = 0; i < 5; ++i) {
      objects.add(new SimpleTestObj("Hallo Welt " + i));
      save(objects.get(i));
    }
    fObjectStore.endUpdate();

    saveAll(objects.toArray());
    reopen();

    List<SimpleTestObj> loadedObjs = query(SimpleTestObj.class);
    loadedObjs.removeAll(objects);
    assertEquals("Save/load mismatch: " + loadedObjs, 0, loadedObjs.size());
  }
  
  public void test_save_object_ref() throws Exception {
    internalTestSaveObjectRef(SimpleTestObj.class);
  }
  
  public void test_save_object_ref_first() throws Exception {
    internalTestSaveObjectRefFirst(SimpleTestObj.class);
  }

  public void test_save_object_ref_first_with_wrongHashCodeObj() throws Exception {
    internalTestSaveObjectRefFirst(WrongHashCode.class);
  }

  public void test_save_object_ref_with_wrong_hashCodeObj() throws Exception {
    internalTestSaveObjectRef(WrongHashCode.class);
  }

  public void test_save_package_scoped_object() {
    Object obj = new PackageScopedTestClass();
    IObjectId id = save(obj);
    
    reopen();
    
    assertTrue(fObjectStore.containsId(id));
  }

  public void test_save_private_inner_scoped_object() {
    OuterClass obj = new OuterClass();
    try {
      save(obj.getInnerClass());
      fail("SchemaCorruptException expected");
    } catch (SchemaException e) {
      //passed
    }
  }

  public void test_save_referencee_in_antoher_transaction() throws Exception {
    internalTestReferenceeInAnotherTransaction(SimpleTestObj.class);
  }

  public void test_save_referencee_in_antoher_transaction_with_wrongHashCodeObj() throws Exception {
    internalTestReferenceeInAnotherTransaction(WrongHashCode.class);
  }

  public void test_save_same_object_twice() {
    WrongHashCode obj = new WrongHashCode();
    save(obj);
    save(obj);

    reopen();
    List<WrongHashCode> objs = query(WrongHashCode.class);
    assertEquals(1, objs.size());
  }

  public void test_save_two_objects_in_two_transactions() {
    SimpleTestObj obj1 = new SimpleTestObj("1");
    SimpleTestObj obj2 = new SimpleTestObj("2");

    save(obj1);
    save(obj2);

    reopen();

    Collections.containsAll(query(SimpleTestObj.class), obj1, obj2);
  }

  private void internalTestReferenceeInAnotherTransaction(Class<?> referenceeType) throws Exception {
    Referencer referencer = new Referencer();
    referencer.set(referenceeType, "1");
    save(referencer.get());
    save(referencer);

    reopen();
    Referencer loadedReferencer = query(Referencer.class).get(0);

    assertEquals(referencer.getStringValueFromReferencee(), loadedReferencer.getStringValueFromReferencee());

    Object loadedObj = query(referenceeType).get(0);
    assertSame(loadedReferencer.get(), loadedObj);
  }

  private void internalTestSaveObjectRef(Class<?> referenceeType) throws Exception {
    Referencer referencer = new Referencer();
    referencer.set(referenceeType, "1");
    saveAll(referencer);

    reopen();

    System.out.println(fObjectStore.getAllObjects());
    
    Referencer loadedReferencer = query(Referencer.class).get(0);
    assertEquals(referencer.getStringValueFromReferencee(), loadedReferencer.getStringValueFromReferencee());

    Object loadedObj = query(referenceeType).get(0);
    assertSame(loadedReferencer.get(), loadedObj);
  }
  
  private void internalTestSaveObjectRefFirst(Class<?> referenceeType) throws Exception {
    Referencer referencer = new Referencer();
    referencer.set(referenceeType, "1");
    save(referencer.get());
    save(referencer);

    assertEquals(1, query(referenceeType).size());

    reopen();
    Referencer loadedReferencer = query(Referencer.class).get(0);

    assertEquals(referencer.getStringValueFromReferencee(), loadedReferencer.getStringValueFromReferencee());

    Object loadedObj = query(referenceeType).get(0);
    assertSame(loadedReferencer.get(), loadedObj);
  }

}
