package org.memoriadb.test.core.handler.array; 


import java.util.Arrays;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.inheritance.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class BasicOneDimensionalArrayTest extends AbstractObjectStoreTest {
  
  public void test_int_array() {
    int[] arr = new int[]{1,2,3};
    IObjectId id = save(arr);
    
    assertEquals(fStore.getIdFactory().getArrayMemoriaClass(), fStore.getMemoriaClassId(arr));
    
    reopen();
    
    int[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    Arrays.equals(arr, arr_l1);
  }
  
  public void test_Integer_array() {
    Integer[] arr = new Integer[]{1,2,3};
    IObjectId id = save(arr);
    
    assertEquals(fStore.getIdFactory().getArrayMemoriaClass(), fStore.getMemoriaClassId(arr));
    
    reopen();
    
    Integer[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    Arrays.equals(arr, arr_l1);
  }
  
  public void test_Integer_array_width_null_value() {
    Integer[] arr = new Integer[]{1,null,3};
    IObjectId id = save(arr);
    
    assertEquals(fStore.getIdFactory().getArrayMemoriaClass(), fStore.getMemoriaClassId(arr));
    
    reopen();
    
    Integer[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    Arrays.equals(arr, arr_l1);
  }
  
  public void test_Object_array() {
    SimpleTestObj[] arr = new SimpleTestObj[]{new SimpleTestObj("1"), new SimpleTestObj("2"), new SimpleTestObj("3")};
    IObjectId id = saveAll(arr);
    
    assertEquals(fStore.getIdFactory().getArrayMemoriaClass(), fStore.getMemoriaClassId(arr));
    
    reopen();
    
    SimpleTestObj[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    Arrays.equals(arr, arr_l1);
  }
  
  public void test_Object_array_width_null_value() {
    SimpleTestObj[] arr = new SimpleTestObj[]{new SimpleTestObj("1"), null, new SimpleTestObj("3"), };
    IObjectId id = saveAll(arr);
    
    assertEquals(fStore.getIdFactory().getArrayMemoriaClass(), fStore.getMemoriaClassId(arr));
    
    reopen();
    
    SimpleTestObj[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    Arrays.equals(arr, arr_l1);
  }
  
  public void test_Object_array_with_inheritance() {
    C[] arr = new C[]{new C(1,2l,"3", true, new SimpleTestObj("4"), (short)5), new C(2,2l,"3", true, new SimpleTestObj("4"), (short)5)};

    IObjectId id = saveAll(arr);
    
    // save a C to get the MemoriaClass of C
    C prototype = new C();
    save(prototype);
    IMemoriaClass classC = fStore.getMemoriaClass(prototype);
    assertEquals(C.class.getName(), classC.getJavaClassName());
    IMemoriaClass classB = classC.getSuperClass();
    assertEquals(B.class.getName(), classB.getJavaClassName());
    
    assertEquals(fStore.getIdFactory().getArrayMemoriaClass(), fStore.getMemoriaClassId(arr));
    
    reopen();
    
    C[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    Arrays.equals(arr, arr_l1);
  }
  
  public void test_string_array() {
    String[] arr = new String[]{"1", "2", "3"};
    IObjectId id = save(arr);
    
    assertEquals(fStore.getIdFactory().getArrayMemoriaClass(), fStore.getMemoriaClassId(arr));
    
    reopen();
    
    String[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    Arrays.equals(arr, arr_l1);
  }
  
}
