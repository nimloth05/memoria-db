package org.memoriadb.test.core.handler.array; 


import java.util.Arrays;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.inheritance.*;
import org.memoriadb.testutil.AbstractMemoriaTest;

public abstract class BasicOneDimensionalArrayTest extends AbstractMemoriaTest {
  
  public void test_int_array() {
    int[] arr = new int[]{1,2,3};
    IObjectId id = save(arr);
    
    assertEquals(fObjectStore.getIdFactory().getArrayMemoriaClass(), fObjectStore.getTypeInfo().getMemoriaClassId(arr));
    
    reopen();
    
    int[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_Integer_array() {
    Integer[] arr = new Integer[]{1,2,3};
    IObjectId id = save(arr);
    
    assertEquals(fObjectStore.getIdFactory().getArrayMemoriaClass(), fObjectStore.getTypeInfo().getMemoriaClassId(arr));
    
    reopen();
    
    Integer[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_Integer_array_width_null_value() {
    Integer[] arr = new Integer[]{1,null,3};
    IObjectId id = save(arr);
    
    assertEquals(fObjectStore.getIdFactory().getArrayMemoriaClass(), fObjectStore.getTypeInfo().getMemoriaClassId(arr));
    
    reopen();
    
    Integer[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_mixed_Object_array() {
    Object[] arr = new Object[]{1,"2", new SimpleTestObj("3")};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    Object[] arr_l1 = get(id);

    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_Object_array() {
    SimpleTestObj[] arr = new SimpleTestObj[]{new SimpleTestObj("1"), new SimpleTestObj("2"), new SimpleTestObj("3")};
    IObjectId id = saveAll(arr);
    
    assertEquals(fObjectStore.getIdFactory().getArrayMemoriaClass(), fObjectStore.getTypeInfo().getMemoriaClassId(arr));
    
    reopen();
    
    SimpleTestObj[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_Object_array_width_null_value() {
    SimpleTestObj[] arr = new SimpleTestObj[]{new SimpleTestObj("1"), null, new SimpleTestObj("3"), };
    IObjectId id = saveAll(arr);
    
    assertEquals(fObjectStore.getIdFactory().getArrayMemoriaClass(), fObjectStore.getTypeInfo().getMemoriaClassId(arr));
    
    reopen();
    
    SimpleTestObj[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_Object_array_with_inheritance() {
    C[] arr = new C[]{new C(1,2l,"3", true, new SimpleTestObj("4"), (short)5), new C(2,2l,"3", true, new SimpleTestObj("4"), (short)5)};

    IObjectId id = saveAll(arr);
    
    // save a C to get the MemoriaClass of C
    C prototype = new C();
    save(prototype);
    
    // check hierarchy
    IMemoriaClass classC = fObjectStore.getTypeInfo().getMemoriaClass(prototype);
    assertEquals(C.class.getName(), classC.getJavaClassName());
    IMemoriaClass classB = classC.getSuperClass();
    assertEquals(B.class.getName(), classB.getJavaClassName());
    IMemoriaClass classA = classB.getSuperClass();
    assertEquals(A.class.getName(), classA.getJavaClassName());
    IMemoriaClass classObject = classA.getSuperClass();
    assertEquals(Object.class.getName(), classObject.getJavaClassName());
    
    assertEquals(fObjectStore.getIdFactory().getArrayMemoriaClass(), fObjectStore.getTypeInfo().getMemoriaClassId(arr));
    
    reopen();
    
    C[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_string_array() {
    String[] arr = new String[]{"1", "2", "3"};
    IObjectId id = save(arr);
    
    assertEquals(fObjectStore.getIdFactory().getArrayMemoriaClass(), fObjectStore.getTypeInfo().getMemoriaClassId(arr));
    
    reopen();
    
    String[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_update_int_array() {
    int[] arr = new int[]{1,2,3};
    IObjectId id = save(arr);
    
    arr[0] = -1;
    save(arr);
    
    reopen();
    
    int[] l1_arr = get(id);
    assertEquals(-1, l1_arr[0]);
    l1_arr[0]=-2;
    save(l1_arr);
    
    reopen();
    int[] l2_arr = get(id);
    assertEquals(-2, l2_arr[0]);
  }
  
}
