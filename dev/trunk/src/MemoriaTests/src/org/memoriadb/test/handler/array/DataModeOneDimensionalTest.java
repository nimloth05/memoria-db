package org.memoriadb.test.handler.array;

import java.util.Arrays;

import org.memoriadb.handler.array.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.SimpleTestObj;
import org.memoriadb.test.testclasses.enums.TestEnum;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class DataModeOneDimensionalTest extends AbstractMemoriaTest {
  
  public void test_add_enum_array() {
    reopenDataMode();
    
    fDataStore.getTypeInfo().addMemoriaClassIfNecessary(TestEnum.class);
    
    IArray arr = fDataStore.getRefactorApi().createArray(TestEnum[].class, 3);
    
    arr.set(0, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), 0));
    arr.set(1, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), 1));
    arr.set(2, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), 2));
    
    IObjectId id = saveAll(arr);
    
    reopen();
    
    TestEnum[] l1_arr = get(id);
    
    assertEquals(3, l1_arr.length);
    assertTrue(Arrays.equals(TestEnum.values(), l1_arr)); 
  }
  
  public void test_create_int_array() {
    reopenDataMode();
    
    IArray arr = fDataStore.getRefactorApi().createArray(int[].class, 2);
     
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
    
    fDataStore.getTypeInfo().addMemoriaClassIfNecessary(SimpleTestObj.class);
    
    IArray arr = fDataStore.getRefactorApi().createArray(SimpleTestObj[].class, 2);
    
    arr.set(0, SimpleTestObj.createFieldObject(fDataStore, "0"));
    arr.set(1, SimpleTestObj.createFieldObject(fDataStore, "1"));
    
    IObjectId id = saveAll(arr);
    
    reopen();
    
    SimpleTestObj[] l1_arr = get(id);
    
    assertEquals(2, l1_arr.length);
  }
  
  public void test_create_Object_array_when_java_class_is_unknown() {
    // add class
    fObjectStore.getTypeInfo().addMemoriaClassIfNecessary(SimpleTestObj.class);
    
    reopenDataMode();
    
    IArray arr = fDataStore.getRefactorApi().createArray(SimpleTestObj.class.getName(), 1, 2);
    
    arr.set(0, SimpleTestObj.createFieldObject(fDataStore, "0"));
    arr.set(1, SimpleTestObj.createFieldObject(fDataStore, "1"));
    
    IObjectId id = saveAll(arr);
    
    reopen();
    
    SimpleTestObj[] l1_arr = get(id);
    
    assertEquals(2, l1_arr.length);
  }

  public void test_int_array() {
    int[] arr = new int[] {0, 1};
    IObjectId id = save(arr);
    
    reopenDataMode();
    
    IDataArray l1_arr = fDataStore.get(id);
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
  
  public void test_modify_enum_in_data_mode() {
    
    TestEnum[] arr = new TestEnum[]{TestEnum.c};
    saveAll(arr);
    
    reopenDataMode();
    
    IArray dataArray = fDataStore.getRefactorApi().createArray(TestEnum[].class, 3);

    dataArray.set(0, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), 0));
    dataArray.set(1, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), 1));
    dataArray.set(2, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), 2));
    
    IObjectId id = saveAll(dataArray);
    
    reopen();
    
    TestEnum[] l1_arr = get(id);
    
    assertEquals(3, l1_arr.length);
    assertTrue(Arrays.equals(TestEnum.values(), l1_arr)); 
  }
  
}
