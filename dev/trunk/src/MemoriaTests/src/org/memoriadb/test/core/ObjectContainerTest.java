package org.memoriadb.test.core;

import java.util.*;

import org.memoriadb.test.core.testclasses.*;
import org.memoriadb.testutil.*;
import org.memoriadb.testutil.Collections;

public class ObjectContainerTest extends AbstractObjectStoreTest {

  public void test_incorrect_hash_code_objects() {
    WrongHashCode obj1 = new WrongHashCode("1");
    WrongHashCode obj2 = new WrongHashCode("2");

    save(obj1, obj2);
    List<WrongHashCode> expectedObjs = Arrays.asList(obj1, obj2);
    reopen();

    List<WrongHashCode> actualObjs = getAll(WrongHashCode.class);

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

  public void test_save_object() {
    List<SimpleTestObj> objects = new ArrayList<SimpleTestObj>();
    for (int i = 0; i < 5; ++i) {
      objects.add(new SimpleTestObj("Hallo Welt " + i, i));
    }

    save(objects.toArray());

    reopen();

    List<SimpleTestObj> loadedObjs = getAll(SimpleTestObj.class);
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

  public void test_save_referencee_in_antoher_transaction() throws Exception {
    internalTestReferenceeInAnotherTransaction(SimpleTestObj.class);
  }

  public void test_save_referencee_in_antoher_transaction_with_wrongHashCodeObj() throws Exception {
    internalTestReferenceeInAnotherTransaction(WrongHashCode.class);
  }

  public void test_save_same_object_twice() {
    WrongHashCode obj = new WrongHashCode();
    save(obj, obj);

    reopen();
    List<WrongHashCode> objs = getAll(WrongHashCode.class);
    assertEquals(1, objs.size());
  }

  public void test_save_two_objects_in_two_transactions() {
    SimpleTestObj obj1 = new SimpleTestObj("1", 1);
    SimpleTestObj obj2 = new SimpleTestObj("2", 2);

    save(obj1);
    save(obj2);

    reopen();

    Collections.containsAll(getAll(SimpleTestObj.class), obj1, obj2);
  }

  public void test_serialize_array_object() {
    ArrayContainer container = new ArrayContainer();
    container.set();

    saveAll(container);
    reopen();

    ArrayContainer loadedContainer = getAll(ArrayContainer.class).get(0);
    SimpleTestObj loadedObj = getAll(SimpleTestObj.class).get(0);

    assertSame(loadedObj, loadedContainer.fArray[0]);
    assertEquals(container.fArray[0], loadedContainer.fArray[0]);
  }

  private void internalTestReferenceeInAnotherTransaction(Class<?> referenceeType) throws Exception {
    Referencer referencer = new Referencer();
    referencer.set(referenceeType, "1");
    save(referencer.get());
    save(referencer);

    reopen();
    Referencer loadedReferencer = getAll(Referencer.class).get(0);

    assertEquals(referencer.getStringValueFromReferencee(), loadedReferencer.getStringValueFromReferencee());

    Object loadedObj = getAll(referenceeType).get(0);
    assertSame(loadedReferencer.get(), loadedObj);
  }

  private void internalTestSaveObjectRef(Class<?> referenceeType) throws Exception {
    Referencer referencer = new Referencer();
    referencer.set(referenceeType, "1");
    saveAll(referencer);

    reopen();

    Referencer loadedReferencer = getAll(Referencer.class).get(0);
    assertEquals(referencer.getStringValueFromReferencee(), loadedReferencer.getStringValueFromReferencee());

    Object loadedObj = getAll(referenceeType).get(0);
    assertSame(loadedReferencer.get(), loadedObj);
  }

  private void internalTestSaveObjectRefFirst(Class<?> referenceeType) throws Exception {
    Referencer referencer = new Referencer();
    referencer.set(referenceeType, "1");
    save(referencer.get(), referencer);

    assertEquals(1, getAll(referenceeType).size());

    reopen();
    Referencer loadedReferencer = getAll(Referencer.class).get(0);

    assertEquals(referencer.getStringValueFromReferencee(), loadedReferencer.getStringValueFromReferencee());

    Object loadedObj = getAll(referenceeType).get(0);
    assertSame(loadedReferencer.get(), loadedObj);
  }

}
