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

import org.memoriadb.handler.array.IArray;
import org.memoriadb.handler.array.IDataArray;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.test.testclasses.enums.TestEnum;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.Arrays;

public class DataModeOneDimensionalTest extends AbstractMemoriaTest {
  
  public void test_add_enum_array() {
    reopenDataMode();
    
    fDataStore.getTypeInfo().addMemoriaClassIfNecessary(TestEnum.class);
    
    IArray arr = fDataStore.getRefactorApi().createArray(TestEnum[].class, 3);
    
    arr.set(0, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), TestEnum.a.name()));
    arr.set(1, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), TestEnum.b.name()));
    arr.set(2, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), TestEnum.c.name()));
    
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
    
    fDataStore.getTypeInfo().addMemoriaClassIfNecessary(StringObject.class);
    
    IArray arr = fDataStore.getRefactorApi().createArray(StringObject[].class, 2);
    
    arr.set(0, StringObject.createFieldObject(fDataStore, "0"));
    arr.set(1, StringObject.createFieldObject(fDataStore, "1"));
    
    IObjectId id = saveAll(arr);
    
    reopen();
    
    StringObject[] l1_arr = get(id);
    
    assertEquals(2, l1_arr.length);
  }
  
  public void test_create_Object_array_when_java_class_is_unknown() {
    // add class
    fObjectStore.getTypeInfo().addMemoriaClassIfNecessary(StringObject.class);
    
    reopenDataMode();
    
    IArray arr = fDataStore.getRefactorApi().createArray(StringObject.class.getName(), 1, 2);
    
    arr.set(0, StringObject.createFieldObject(fDataStore, "0"));
    arr.set(1, StringObject.createFieldObject(fDataStore, "1"));
    
    IObjectId id = saveAll(arr);
    
    reopen();
    
    StringObject[] l1_arr = get(id);
    
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

    dataArray.set(0, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), TestEnum.a.name()));
    dataArray.set(1, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), TestEnum.b.name()));
    dataArray.set(2, fDataStore.getRefactorApi().getEnum(TestEnum.class.getName(), TestEnum.c.name()));
    
    IObjectId id = saveAll(dataArray);
    
    reopen();
    
    TestEnum[] l1_arr = get(id);
    
    assertEquals(3, l1_arr.length);
    assertTrue(Arrays.equals(TestEnum.values(), l1_arr)); 
  }
  
}
