package org.memoriadb.test.core.crud;

import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.crud.testclass.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;
import org.memoriadb.util.Constants;

public class DeleteTest extends AbstractObjectStoreTest {
  
  public void test_add_and_delete_in_same_transaction(){
    
    for(Object obj: fStore.getAllObjects()) {
      System.out.println(fStore.getObjectId(obj) + " metaClassId " + fStore.getObjectId(fStore.getMemoriaClass(obj)));
    }
    
    OneInt a = new OneInt(0);
    beginUpdate();
      IObjectId a_id = save(a);
      delete(a);
    endUpdate();
    
    assertFalse(fStore.contains(a));
    
    reopen();
    
    assertFalse(fStore.contains(a_id));
  }
  
  public void test_basic_delete() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    delete(a);
    
    assertFalse(fStore.contains(a_id));
    IObjectInfo info = fStore.getObjectInfo(a_id);
    assertTrue(info.isDeleted());
    assertEquals(Constants.INITIAL_VERSION+1, info.getVersion());
    assertEquals(1, info.getOldGenerationCount());
    
    reopen();
    
    assertFalse(fStore.contains(a_id));
    
    info = fStore.getObjectInfo(a_id);
    assertEquals(Constants.INITIAL_VERSION+1, info.getVersion());
    assertEquals(1, info.getOldGenerationCount());
    
  }
  
  public void test_delete_aggregate() {
    A a = new A(new B("b"));
    IObjectId a_id = saveAll(a);
    IObjectId b_id = fStore.getObjectId(a.getB());
    
    deleteAll(a);
    
    assertFalse(fStore.contains(a_id));
    assertFalse(fStore.contains(b_id));
    
    reopen();

    assertFalse(fStore.contains(a_id));
    assertFalse(fStore.contains(b_id));
    
    IObjectInfo a_info = fStore.getObjectInfo(a_id);
    assertTrue(a_info.isDeleted());
    assertEquals(Constants.INITIAL_VERSION+1, a_info.getVersion());
    assertEquals(1, a_info.getOldGenerationCount());

    IObjectInfo b_info = fStore.getObjectInfo(b_id);
    assertTrue(b_info.isDeleted());
    assertEquals(Constants.INITIAL_VERSION+1, b_info.getVersion());
    assertEquals(1, b_info.getOldGenerationCount());

  }
  
  public void test_delete_aggregate_and_add_it_again() {
    A a = new A(new B("b"));
    saveAll(a);
    
    deleteAll(a);
    assertFalse(fStore.contains(a));
    assertFalse(fStore.contains(a.getB()));
    
    saveAll(a);
    assertTrue(fStore.contains(a));
    assertTrue(fStore.contains(a.getB()));
  }
  
  public void test_update_and_delete_in_same_transaction() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    
    reopen();
    
    OneInt a_l1 = fStore.getObject(a_id);
    
    a_l1.setInt(1);
    beginUpdate();
      save(a_l1);
      delete(a_l1);
    endUpdate();
    
    IObjectInfo info = fStore.getObjectInfo(a_id);
    assertEquals(Constants.INITIAL_VERSION+1, info.getVersion());
    assertEquals(1, info.getOldGenerationCount());
    assertTrue(info.isDeleted());
    
  }
  
}
