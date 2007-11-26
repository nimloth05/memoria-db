package org.memoriadb.test.core.handler.array;

import org.memoriadb.core.handler.array.*;
import org.memoriadb.core.handler.field.FieldObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class DataModeOneDimensionalTest extends AbstractObjectStoreTest {
  
  public void test_create_int_array() {
    reopenDataMode();
    
    IArray arr = fDataStore.refactorApi().createArray(int[].class, 2);
     
    arr.set(0, 0);
    arr.set(1, 1);
    
    IObjectId id = save(arr);
    
    reopen();
    
    int[] l1_arr = get(id);
    
    assertEquals(2, l1_arr.length);
    assertEquals(0, l1_arr[0]);
    assertEquals(1, l1_arr[1]);
  }
  
  public void test_create_Object_array_when_java_class_is_known() {
    reopenDataMode();
    
    fDataStore.typeInfo().addMemoriaClass(SimpleTestObj.class);
    
    IArray arr = fDataStore.refactorApi().createArray(SimpleTestObj[].class, 2);
    
    IObjectId memoriaClassId = fDataStore.typeInfo().getMemoriaClassId(SimpleTestObj.class);
    fDataStore.refactorApi().arraySet(0, new SimpleTestObj("0"));
    arr.set(0, new FieldObject(new SimpleTestObj("0"), memoriaClassId));
    arr.set(1, new FieldObject(new SimpleTestObj("1"), memoriaClassId));
    
    IObjectId id = saveAll(arr);
    
    reopen();
    
    SimpleTestObj[] l1_arr = get(id);
    
    assertEquals(2, l1_arr.length);
  }
  
  public void test_create_Object_array_when_java_class_is_unknown() {
    // add class
    fObjectStore.typeInfo().addMemoriaClass(SimpleTestObj.class);
    
    reopenDataMode();
    
    IArray arr = fDataStore.refactorApi().createArray(SimpleTestObj.class.getName(), 1, 2);
     
    IObjectId memoriaClassId = fDataStore.typeInfo().getMemoriaClassId(SimpleTestObj.class);
    
    arr.set(0, new FieldObject(new SimpleTestObj("0"), memoriaClassId));
    arr.set(1, new FieldObject(new SimpleTestObj("0"), memoriaClassId));
    
    IObjectId id = saveAll(arr);
    
    reopen();
    
    SimpleTestObj[] l1_arr = get(id);
    
    assertEquals(2, l1_arr.length);
  }
  
  public void test_int_array() {
    int[] arr = new int[]{0,1};
    IObjectId id = save(arr);
    
    reopenDataMode();
    
    IDataArray l1_arr = fDataStore.getObject(id);
    assertEquals(0, l1_arr.get(0));
    assertEquals(1, l1_arr.get(1));
    
    // change array in data mode
    l1_arr.set(0, -1);
    l1_arr.add(2);
    
    save(l1_arr);
    
    reopen();
    
    int[] l2_arr = get(id);
    assertEquals(-1, l2_arr[0]);
    assertEquals(1, l2_arr[1]);
    assertEquals(2, l2_arr[2]);
  }
  
}
