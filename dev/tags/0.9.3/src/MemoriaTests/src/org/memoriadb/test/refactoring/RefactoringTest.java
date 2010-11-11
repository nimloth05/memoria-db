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

package org.memoriadb.test.refactoring;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.refactoring.old.Address;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class RefactoringTest extends AbstractMemoriaTest {

  public void test_change_className() {

    // Setup
    Address address = new org.memoriadb.test.refactoring.old.Address();
    address.setAddress("Test Address");
    IObjectId objectId = save(address);
    System.out.println(objectId);
    reopenDataMode();
    
//    IDataObject obj = fDataStore.get(objectId);
//    IMemoriaClass memCls = fDataStore.get(obj.getMemoriaClassId());
//    System.out.println(memCls.getClass().getName());
//    System.out.println(memCls.getJavaClassName());
//    System.out.println(MemoriaClass.class.getName()); 

   // change meta-data
    IMemoriaClass memClass = fDataStore.getRefactorApi().renameClass(org.memoriadb.test.refactoring.old.Address.class.getName(), org.memoriadb.test.refactoring.nu.Address.class.getName());
    assertNotNull("Rename failed - class not found", memClass);
    assertEquals(org.memoriadb.test.refactoring.nu.Address.class.getName(), memClass.getJavaClassName());
  }
}
