package org.memoriadb.test.core.valueobject;

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
  
}
