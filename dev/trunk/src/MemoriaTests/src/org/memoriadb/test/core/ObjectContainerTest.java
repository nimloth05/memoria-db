/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.test.core;

import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.util.Constants;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.*;
import org.memoriadb.testutil.AbstractMemoriaTest;
import org.memoriadb.testutil.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ObjectContainerTest extends AbstractMemoriaTest {

  public void test_contains() {
    StringObject obj = new StringObject();
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
    List<StringObject> objects = new ArrayList<StringObject>();
    fObjectStore.beginUpdate();
    for (int i = 0; i < 5; ++i) {
      objects.add(new StringObject("Hallo Welt " + i));
      save(objects.get(i));
    }
    fObjectStore.endUpdate();

    saveAll(objects.toArray());
    reopen();

    List<StringObject> loadedObjs = query(StringObject.class);
    loadedObjs.removeAll(objects);
    assertEquals("Save/load mismatch: " + loadedObjs, 0, loadedObjs.size());
  }
  
  public void test_save_object_ref() throws Exception {
    internalTestSaveObjectRef(StringObject.class);
  }
  
  public void test_save_object_ref_first() throws Exception {
    internalTestSaveObjectRefFirst(StringObject.class);
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
      save(obj.getPrivateInnerClass());
      fail("SchemaCorruptException expected");
    } catch (SchemaException e) {
      //passed
    }
  }

  public void test_save_referencee_in_antoher_transaction() throws Exception {
    internalTestReferenceeInAnotherTransaction(StringObject.class);
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
    StringObject obj1 = new StringObject("1");
    StringObject obj2 = new StringObject("2");

    save(obj1);
    save(obj2);

    reopen();

    CollectionUtil.containsAll(query(StringObject.class), obj1, obj2);
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
