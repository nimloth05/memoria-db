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

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.test.testclasses.TestValueObject;
import org.memoriadb.test.testclasses.inheritance.A;
import org.memoriadb.test.testclasses.inheritance.B;
import org.memoriadb.test.testclasses.inheritance.C;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BasicMultiDimensionalArrayTest extends AbstractMemoriaTest {

  public void test_int_array() {
    int[][] arr = new int[][]{new int[]{1,2}, new int[]{3,4,5}};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    int[][] l1_arr = get(id);
    Arrays.deepEquals(arr, l1_arr);
  }
  
  public void test_int_array_using_save() {
    int[][] arr = new int[][]{new int[]{1,2}, new int[]{3,4,5}};
    save(arr[0]);
    save(arr[1]);
    IObjectId id = save(arr);
    
    reopen();
    
    int[][] l1_arr = get(id);
    Arrays.deepEquals(arr, l1_arr);
  }

  public void test_Integer_array() {
    Integer[][] arr = new Integer[][]{new Integer[]{1,2}, new Integer[]{3,4,5}};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    Integer[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }

  public void test_Integer_array_width_null_value() {
    Integer[][] arr = new Integer[][]{new Integer[]{1,null},null};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    Integer[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }
  
  public void test_mixed_Object_array() {
    Object[][] arr = new Object[][]{new Object[]{1,new Object[]{3,4, "5"}}, new Object[]{new ArrayList<Integer>(), "7"}};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    Object[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }
  
  public void test_Object_array() {
    StringObject[][] arr = new StringObject[][]{new StringObject[]{new StringObject("1"),new StringObject("2")}, new StringObject[]{new StringObject("3"),new StringObject("4")}};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    StringObject[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }

  public void test_Object_array_with_interitance() {
    C[][] arr = new C[][]{new C[]{getC(), getC()}, new C[]{getC()}};

    IObjectId id = saveAll(arr);

    // plus 3 classes: A, B, C 
    assertNotNull(fObjectStore.getTypeInfo().getMemoriaClass(A.class));
    assertNotNull(fObjectStore.getTypeInfo().getMemoriaClass(B.class));
    assertNotNull(fObjectStore.getTypeInfo().getMemoriaClass(C.class));
    assertTypeHierachy(C.class);
    
    reopen();
    
    C[][] l1_arr = get(id);

    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }
  
  public void test_Object_array_with_null_value() {
    StringObject[][] arr = new StringObject[][]{new StringObject[]{new StringObject("1"),null}, null};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    StringObject[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }
  
  public void test_save_valueObject() {
    TestValueObject[][] array = new TestValueObject[3][3];
    for(int i = 0; i < 3; ++i) {
      for (int j = 0; i < 3; ++i) {
        array[i][j] = new TestValueObject(Integer.toString(i));
      }
    }
    
    IObjectId id = saveAll(array);
    
    assertFalse(fObjectStore.contains(array[0][0]));
    
    reopen();
    
    TestValueObject[][] l1_array = fObjectStore.get(id);
    assertTrue(Arrays.deepEquals(array, l1_array));
    assertFalse(fObjectStore.contains(l1_array[0][0]));
  }

  private C getC() {
    return new C(1,2l,"3", true, null, (short)7);
  }
  
}
