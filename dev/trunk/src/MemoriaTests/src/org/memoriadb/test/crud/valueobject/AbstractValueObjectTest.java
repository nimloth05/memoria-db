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

package org.memoriadb.test.crud.valueobject;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.mode.AbstractStore;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.crud.valueobject.testclasses.ValueB;
import org.memoriadb.test.testclasses.ObjectReferencer;
import org.memoriadb.test.testclasses.TestValueObject;
import org.memoriadb.test.testclasses.ValueObjectReferencer;
import org.memoriadb.testutil.AbstractMemoriaTest;
import org.memoriadb.testutil.CollectionUtil;

public abstract class AbstractValueObjectTest extends AbstractMemoriaTest {
  
  private static final String OBJECT_REFERENCER_FIELD_NAME = "fObject";

  public void test_delete() {
    ObjectReferencer ref = new ObjectReferencer();
    TestValueObject valueObject = new TestValueObject("1"); 
    ref.setObject(valueObject);
    
    assertFalse(fObjectStore.contains(valueObject));
    fObjectStore.delete(ref);
    assertFalse(fObjectStore.contains(valueObject));
    
    reopen();
    assertEquals(0, CollectionUtil.count(fObjectStore.getAllUserSpaceObjects()));
  }
  
  public void test_deleteAll() {
    ObjectReferencer ref = new ObjectReferencer();
    TestValueObject valueObject = new TestValueObject("1"); 
    ref.setObject(valueObject);
    
    assertFalse(fObjectStore.contains(valueObject));
    fObjectStore.deleteAll(ref);
    assertFalse(fObjectStore.contains(valueObject));
    
    reopen();
    assertEquals(0, CollectionUtil.count(fObjectStore.getAllUserSpaceObjects()));
  }
  
  public void test_inheritance() {
    ObjectReferencer ref = new ObjectReferencer(new ValueB("a", "b"));
    
    IObjectId id = save(ref);
    assertFalse(fObjectStore.contains(ref.getObject()));
    
    reopen();
    
    ref = get(id);
    
    assertFalse(fObjectStore.contains(ref.getObject()));
    assertEquals("a", ((ValueB)ref.getObject()).fDataA);
  }
  
  public void test_save_valueObject_owner() {
    ObjectReferencer ref = new ObjectReferencer();
    TestValueObject valueObject = new TestValueObject("1"); 
    ref.setObject(valueObject);
    
    IObjectId refId = save(ref);
    assertFalse(fObjectStore.contains(valueObject));
    
    reopen();
    
    ObjectReferencer l1_ref = fObjectStore.get(refId);
    assertFalse(fObjectStore.contains(l1_ref.getObject()));
    assertEquals(ref.getObject(), l1_ref.getObject());
    assertEquals(1, CollectionUtil.count(fObjectStore.getAllUserSpaceObjects()));
  }
  
  public void test_save_valueObject_throws() {

    try {
      save(new TestValueObject("1"));
      fail("saving value-object is not allwed");
    }
    catch(Exception e) {
      // pass
    }
    
  }
  
  public void test_saveAll_valueObject_owner() {
    ObjectReferencer ref = new ObjectReferencer();
    TestValueObject valueObject = new TestValueObject("1"); 
    ref.setObject(valueObject);
    
    IObjectId refId = saveAll(ref);
    assertFalse(fObjectStore.contains(valueObject));
    
    reopen();
    
    ObjectReferencer l1_ref = fObjectStore.get(refId);
    assertFalse(fObjectStore.contains(l1_ref.getObject()));
    assertEquals(ref.getObject(), l1_ref.getObject());
    assertEquals(1, CollectionUtil.count(fObjectStore.getAllUserSpaceObjects()));
  }
  
  public void test_share_reference_in_dataMode() {
    ObjectReferencer ref = new ObjectReferencer();
    ref.setObject(new TestValueObject("1"));
    
    IObjectId id = save(ref);
    reopenDataMode();
    
    IFieldbasedObject l1_ref = fDataStore.get(id);
    IFieldbasedObject l1_valueObject = (IFieldbasedObject) l1_ref.get(OBJECT_REFERENCER_FIELD_NAME);
    
    IFieldbasedObject l1_ref2 = fDataStore.getRefactorApi().asFieldDataObject(new ObjectReferencer());
    l1_ref2.set(OBJECT_REFERENCER_FIELD_NAME, l1_valueObject);
    
    IObjectId id2 = save(l1_ref2);
    
    reopen();
    
    ObjectReferencer l2_ref1 = fObjectStore.get(id);
    ObjectReferencer l2_ref2 = fObjectStore.get(id2);
    assertNotSame(l2_ref1.getObject(), l2_ref2.getObject());
  }
  
  public void test_update_valueObject_in_dataMode() {
    ObjectReferencer ref = new ObjectReferencer();
    ref.setObject(new TestValueObject("1"));
    
    IObjectId id = save(ref);
    reopenDataMode();
    
    IFieldbasedObject l1_ref = fDataStore.get(id);
    IFieldbasedObject l1_valueObject = (IFieldbasedObject) l1_ref.get(OBJECT_REFERENCER_FIELD_NAME);
    l1_valueObject.set("fValue", fDataStore.getRefactorApi().getLangValueObject("2"));
    
    save(l1_ref);
    
    reopen();
    
    ObjectReferencer l2_ref = fObjectStore.get(id);
    assertEquals(new TestValueObject("2"), l2_ref.getObject());
  }
  
  public void test_value_object_metaInfo_in_dataMode() {
    ObjectReferencer ref = new ObjectReferencer();
    ref.setObject(new TestValueObject("1"));
    IObjectId refId = fObjectStore.save(ref);
    
    reopenDataMode();
    
    IFieldbasedObject object = fDataStore.get(refId);
    Object l1_valueObject = object.get(OBJECT_REFERENCER_FIELD_NAME);
    
    IMemoriaClass memoriaClass = fDataStore.getTypeInfo().getMemoriaClass(l1_valueObject);
    assertEquals(TestValueObject.class.getName(), memoriaClass.getJavaClassName());
    assertNull(((AbstractStore)fDataStore).getObjectInfo(l1_valueObject));
  }
  
  public void test_valueObject_in_data_mode() {
    ObjectReferencer ref = new ObjectReferencer();
    ref.setObject(new TestValueObject("1"));
    
    IObjectId id = save(ref);
    reopenDataMode();
    
    IFieldbasedObject l1_ref = fDataStore.get(id);
    IFieldbasedObject l1_valueObject = (IFieldbasedObject) l1_ref.get(OBJECT_REFERENCER_FIELD_NAME);
    assertTrue(l1_valueObject.equalsLangValueObject("fValue", "1"));
  }
  
  /**
   * ObjectReferencer->ValueObjectReferencer->TestValueObject
   */
  public void test_valueObject_references_a_valueObject() {
    ObjectReferencer ref = new ObjectReferencer();
    ValueObjectReferencer valueObjectRef = new ValueObjectReferencer();
    TestValueObject valueObject = new TestValueObject("1");
    
    ref.setObject(valueObjectRef);
    valueObjectRef.setObject(valueObject);
    IObjectId id = save(ref);
    
    reopen();
    
    ObjectReferencer l1_ref = fObjectStore.get(id);
    ValueObjectReferencer l1_valueObjectRef = (ValueObjectReferencer) l1_ref.getObject();
    assertNotNull(l1_valueObjectRef);
    
    TestValueObject l1_valueObject = (TestValueObject) l1_valueObjectRef.getObject();
    assertNotNull(l1_valueObject);
    assertEquals("1", l1_valueObject.getValue());
  }
  
}
