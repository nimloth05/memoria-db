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
