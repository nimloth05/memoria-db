package org.memoriadb.core;

import java.util.*;

import org.memoriadb.core.testclasses.*;

public class ObjectContainerTest extends AbstractFileStoreTest {
  
  public void test_incorrect_hash_code_objects() {
    WrongHashCode obj1 = new WrongHashCode("1");
    WrongHashCode obj2 = new WrongHashCode("2");
    
    List<Object> expectedObjs = save(obj1, obj2);
    reopen();
    
    List<WrongHashCode> actualObjs = getAll(WrongHashCode.class);
    
    Iterator<Object> expectedIter = expectedObjs.iterator();
    while(expectedIter.hasNext()) {
      WrongHashCode savedObj = (WrongHashCode) expectedIter.next();
      
      Iterator<WrongHashCode> actualIter = actualObjs.iterator();
      while(actualIter.hasNext()) {
        if (savedObj.fValue.equals(actualIter.next().fValue)) actualIter.remove(); 
      }
    }
    assertTrue("Not all objects where loaded/saved: "+ actualObjs, actualObjs.isEmpty());
  }
  
  public void test_save_object() {
    List<SimpleTestObj> objects = new ArrayList<SimpleTestObj>();
    for(int i = 0; i < 5; ++i) {
      objects.add(new SimpleTestObj("Hallo Welt "+i, i));
    }
    
    save(objects.toArray());
    
    reopen();
    
    List<SimpleTestObj> loadedObjs = getAll(SimpleTestObj.class);
    loadedObjs.removeAll(objects);
    assertEquals("Save/load mismatch: " +loadedObjs, 0, loadedObjs.size());
  }
  
  public void test_save_object_ref_first() throws Exception {
    internalTestSaveObjectRefFirst(TestObj.class);
  }
  
  public void test_save_object_ref_first_with_wrongHashCodeObj() throws Exception {
    internalTestSaveObjectRefFirst(WrongHashCode.class); 
  }
  
  public void test_save_referencee_in_antoher_transaction() throws Exception {
    internalTestReferenceeInAnotherTransaction(TestObj.class);
  }
  
  public void test_save_referencee_in_antoher_transaction_with_wrongHashCodeObj() throws Exception {
    internalTestReferenceeInAnotherTransaction(WrongHashCode.class);
  }

  //We need Java-Serialization first
//  public void test_save_single_string() {
//    String expected = "A";
//    save(expected);
//    reopen();
//    String actual = getAll(String.class).get(0);
//    assertEquals(expected, actual);
//  }
  
  public void test_save_same_object_twice() {
    WrongHashCode obj = new WrongHashCode();
    save(obj, obj, obj);
    
    reopen();
    List<WrongHashCode> objs = getAll(WrongHashCode.class);
    assertEquals(1, objs.size());
  }
  
  public void test_save_two_objects_in_two_transactions() {
    TestObj obj1 = new TestObj("1", 1);
    TestObj obj2 = new TestObj("2", 2);
    
    save(obj1);
    save(obj2);
    
    reopen();
    
    List<TestObj> objs = getAll(TestObj.class);
    assertEquals(obj1.getString(), objs.get(0).getString());
    assertEquals(obj2.getString(), objs.get(1).getString());
  }
  
  public void test_svae_object_ref() throws Exception {
    internalTestSaveObjectRef(TestObj.class);
  }
  
  public void test_svae_object_ref_with_wrong_hashCodeObj() throws Exception {
    internalTestSaveObjectRef(WrongHashCode.class);
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
    save(referencer);
    
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
