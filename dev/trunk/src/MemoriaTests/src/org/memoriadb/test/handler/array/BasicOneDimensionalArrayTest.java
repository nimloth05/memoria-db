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

package org.memoriadb.test.handler.array;


import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.test.testclasses.TestValueObject;
import org.memoriadb.test.testclasses.inheritance.A;
import org.memoriadb.test.testclasses.inheritance.B;
import org.memoriadb.test.testclasses.inheritance.C;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.Arrays;

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
    Object[] arr = new Object[]{1,"2", new StringObject("3")};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    Object[] arr_l1 = get(id);

    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_Object_array() {
    StringObject[] arr = new StringObject[]{new StringObject("1"), new StringObject("2"), new StringObject("3")};
    IObjectId id = saveAll(arr);
    
    assertEquals(fObjectStore.getIdFactory().getArrayMemoriaClass(), fObjectStore.getTypeInfo().getMemoriaClassId(arr));
    
    reopen();
    
    StringObject[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_Object_array_width_null_value() {
    StringObject[] arr = new StringObject[]{new StringObject("1"), null, new StringObject("3"), };
    IObjectId id = saveAll(arr);
    
    assertEquals(fObjectStore.getIdFactory().getArrayMemoriaClass(), fObjectStore.getTypeInfo().getMemoriaClassId(arr));
    
    reopen();
    
    StringObject[] arr_l1 = get(id);

    assertNotSame(arr, arr_l1);
    assertTrue(Arrays.equals(arr, arr_l1));
  }
  
  public void test_Object_array_with_inheritance() {
    C[] arr = new C[]{new C(1,2l,"3", true, new StringObject("4"), (short)5), new C(2,2l,"3", true, new StringObject("4"), (short)5)};

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
  
  public void test_save_valueObject() {
    TestValueObject[] array = new TestValueObject[3];
    for(int i = 0; i < 3; ++i) {
      array[i] = new TestValueObject(Integer.toString(i));
    }
    
    IObjectId id = save(array);
    
    reopen();
    
    assertTrue(Arrays.deepEquals(array, (Object[]) fObjectStore.get(id)));
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
