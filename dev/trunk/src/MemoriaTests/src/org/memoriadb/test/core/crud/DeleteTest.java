package org.memoriadb.test.core.crud;

import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.crud.testclass.*;
import org.memoriadb.test.core.testclasses.OneInt;
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
    
    assertFalse(fStore.containsId(a_id));
  }
  
  public void test_basic_delete() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    delete(a);
    
    assertFalse(fStore.containsId(a_id));
    IObjectInfo info = fStore.getObjectInfo(a_id);
    assertTrue(info.isDeleted());
    assertEquals(Constants.INITIAL_REVISION+1, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());
    
    reopen();
    
    assertFalse(fStore.containsId(a_id));
    
    info = fStore.getObjectInfo(a_id);
    assertEquals(Constants.INITIAL_REVISION+1, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());
  }
  
  public void test_delete_aggregate() {
    A a = new A(new B("b"));
    IObjectId a_id = saveAll(a);
    IObjectId b_id = fStore.getObjectId(a.getB());
    
    deleteAll(a);
    
    assertFalse(fStore.containsId(a_id));
    assertFalse(fStore.containsId(b_id));
    
    reopen();

    assertFalse(fStore.containsId(a_id));
    assertFalse(fStore.containsId(b_id));
    
    IObjectInfo a_info = fStore.getObjectInfo(a_id);
    assertTrue(a_info.isDeleted());
    assertEquals(Constants.INITIAL_REVISION+1, a_info.getRevision());
    assertEquals(1, a_info.getOldGenerationCount());

    IObjectInfo b_info = fStore.getObjectInfo(b_id);
    assertTrue(b_info.isDeleted());
    assertEquals(Constants.INITIAL_REVISION+1, b_info.getRevision());
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
  
  public void test_delete_and_readd() {
    Object o1 = new Object();
    IObjectId id1 = save(o1);
    delete(o1);
    IObjectId id2 = save(o1);
    assertFalse(id1.equals(id2));
  }
  
  public void test_delete_and_readd_in_same_trx() {
    beginUpdate();
    
    Object o1 = new Object();
    IObjectId id1 = save(o1);
    delete(o1);
    IObjectId id2 = save(o1);
    assertFalse(id1.equals(id2));
    
    endUpdate();
  }
  
  public void test_delete_not_contained_object() {
    Object object = new Object();
    assertFalse(fStore.contains(object));
    
    fStore.deleteAll(object);
    fStore.delete(object);
    
    assertFalse(fStore.contains(object));
  }
  
  public void test_save_and_delete_in_same_transaction() {
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
    assertEquals(Constants.INITIAL_REVISION+1, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());
    assertTrue(info.isDeleted());
    
  }
  
  public void test_save_object_after_deletion() {
    Object object = new Object();
    assertFalse(fStore.contains(object));
    
    fStore.deleteAll(object);
    fStore.delete(object);
    
    assertFalse(fStore.contains(object));
    
    IObjectId id = save(object);
    
    assertTrue(fStore.contains(object));
    assertTrue(fStore.containsId(id));
  }
  
}
