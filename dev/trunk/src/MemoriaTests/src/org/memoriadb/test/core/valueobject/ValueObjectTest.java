package org.memoriadb.test.core.valueobject;

import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.*;
import org.memoriadb.testutil.*;

public class ValueObjectTest extends AbstractMemoriaTest {
  
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
  
  public void test_save_valueObject_owner() {
    ObjectReferencer ref = new ObjectReferencer();
    TestValueObject valueObject = new TestValueObject("1"); 
    ref.setObject(valueObject);
    
    IObjectId refId = save(ref);
    assertFalse(fObjectStore.contains(valueObject));
    
    reopen();
    
    ObjectReferencer l1_ref = fObjectStore.get(refId);
    assertFalse(fObjectStore.contains(l1_ref.getObejct()));
    assertEquals(ref.getObejct(), l1_ref.getObejct());
    assertEquals(1, CollectionUtil.count(fObjectStore.getAllUserSpaceObjects()));
  }
  
  public void test_saveAll_valueObject_owner() {
    ObjectReferencer ref = new ObjectReferencer();
    TestValueObject valueObject = new TestValueObject("1"); 
    ref.setObject(valueObject);
    
    IObjectId refId = saveAll(ref);
    assertFalse(fObjectStore.contains(valueObject));
    
    reopen();
    
    ObjectReferencer l1_ref = fObjectStore.get(refId);
    assertFalse(fObjectStore.contains(l1_ref.getObejct()));
    assertEquals(ref.getObejct(), l1_ref.getObejct());
    assertEquals(1, CollectionUtil.count(fObjectStore.getAllUserSpaceObjects()));
  }
  
  public void test_share_reference_in_dataMode() {
    ObjectReferencer ref = new ObjectReferencer();
    ref.setObject(new TestValueObject("1"));
    
    IObjectId id = save(ref);
    reopenDataMode();
    
    IFieldbasedObject l1_ref = fDataStore.get(id);
    IFieldbasedObject l1_valueObject = (IFieldbasedObject) l1_ref.get("fObject");
    
    IFieldbasedObject l1_ref2 = fDataStore.getRefactorApi().asFieldDataObject(new ObjectReferencer());
    l1_ref2.set("fObject", l1_valueObject);
    
    IObjectId id2 = save(l1_ref2);
    
    reopen();
    
    ObjectReferencer l2_ref1 = fObjectStore.get(id);
    ObjectReferencer l2_ref2 = fObjectStore.get(id2);
    assertNotSame(l2_ref1.getObejct(), l2_ref2.getObejct());
    assertEquals(l2_ref1.getObejct(), l2_ref2.getObejct());
  }
  
  public void test_update_valueObject_in_dataMode() {
    ObjectReferencer ref = new ObjectReferencer();
    ref.setObject(new TestValueObject("1"));
    
    IObjectId id = save(ref);
    reopenDataMode();
    
    IFieldbasedObject l1_ref = fDataStore.get(id);
    IFieldbasedObject l1_valueObject = (IFieldbasedObject) l1_ref.get("fObject");
    l1_valueObject.set("fValue", "2");
    
    save(l1_ref);
    
    reopen();
    
    ObjectReferencer l2_ref = fObjectStore.get(id);
    assertEquals(new TestValueObject("2"), l2_ref.getObejct());
  }
  
  public void test_valueObject_in_data_mode() {
    ObjectReferencer ref = new ObjectReferencer();
    ref.setObject(new TestValueObject("1"));
    
    IObjectId id = save(ref);
    reopenDataMode();
    
    IFieldbasedObject l1_ref = fDataStore.get(id);
    IFieldbasedObject l1_valueObject = (IFieldbasedObject) l1_ref.get("fObject");
    assertEquals("1", l1_valueObject.get("fValue"));
  }
  
  public void test_valueObject_references_a_valueObject() {
    ObjectReferencer ref = new ObjectReferencer();
    ValueObjectReferencer valueObjectRef = new ValueObjectReferencer();
    TestValueObject valueObject = new TestValueObject("1");
    
    ref.setObject(valueObjectRef);
    valueObjectRef.setObject(valueObject);
    IObjectId id = save(ref);
    
    reopen();
    
    ObjectReferencer l1_ref = fObjectStore.get(id);
    ValueObjectReferencer l1_valueObjectRef = (ValueObjectReferencer) l1_ref.getObejct();
    assertNotNull(l1_valueObjectRef);
    
    TestValueObject l1_valueObject = (TestValueObject) l1_valueObjectRef.getObject();
    assertNotNull(l1_valueObject);
    assertEquals("1", l1_valueObject.getValue());
  }
  
}
