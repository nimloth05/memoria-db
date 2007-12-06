package org.memoriadb.test.crud.delete;

import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.util.Constants;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.crud.testclass.*;
import org.memoriadb.test.testclasses.OneInt;
import org.memoriadb.testutil.AbstractMemoriaTest;

public abstract class DeleteTest extends AbstractMemoriaTest {
  
  public void test_add_and_delete_in_same_transaction(){
    
    OneInt a = new OneInt(0);
    beginUpdate();
      IObjectId a_id = save(a);
      delete(a);
    endUpdate();
    
    assertFalse(fObjectStore.contains(a));
    
    reopen();
    
    assertFalse(fObjectStore.containsId(a_id));
  }
  
  public void test_basic_delete() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    delete(a);
    
    assertFalse(fObjectStore.containsId(a_id));
    IObjectInfo info = fObjectStore.getObjectInfoForId(a_id);
    assertTrue(info.isDeleted());
    assertEquals(Constants.INITIAL_REVISION + 2, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());
    
    reopen();
    
    assertFalse(fObjectStore.containsId(a_id));
    
    info = fObjectStore.getObjectInfoForId(a_id);
    assertEquals(Constants.INITIAL_REVISION + 2, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());
  }
  
  public void test_delete_aggregate() {
    A a = new A(new B("b"));
    IObjectId a_id = saveAll(a);
    IObjectId b_id = fObjectStore.getId(a.getB());
    
    deleteAll(a);
    
    assertFalse(fObjectStore.containsId(a_id));
    assertFalse(fObjectStore.containsId(b_id));
    
    reopen();

    assertFalse(fObjectStore.containsId(a_id));
    assertFalse(fObjectStore.containsId(b_id));
    
    IObjectInfo a_info = fObjectStore.getObjectInfoForId(a_id);
    assertTrue(a_info.isDeleted());
    assertEquals(Constants.INITIAL_REVISION + 2, a_info.getRevision());
    assertEquals(1, a_info.getOldGenerationCount());

    IObjectInfo b_info = fObjectStore.getObjectInfoForId(b_id);
    assertTrue(b_info.isDeleted());
    assertEquals(Constants.INITIAL_REVISION + 2, b_info.getRevision());
    assertEquals(1, b_info.getOldGenerationCount());

  }
  
  public void test_delete_aggregate_and_add_it_again() {
    A a = new A(new B("b"));
    saveAll(a);
    
    deleteAll(a);
    assertFalse(fObjectStore.contains(a));
    assertFalse(fObjectStore.contains(a.getB()));
    
    saveAll(a);
    assertTrue(fObjectStore.contains(a));
    assertTrue(fObjectStore.contains(a.getB()));
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
    assertFalse(fObjectStore.contains(object));
    
    fObjectStore.deleteAll(object);
    fObjectStore.delete(object);
    
    assertFalse(fObjectStore.contains(object));
  }
  
  public void test_delete_referencee() {
    A a = new A(new B("b"));
    
    IObjectId id = saveAll(a);
    
    // delete the referenced object
    delete(a.getB());
    reopen();
    
    A l1_a = get(id);
    
    assertNull(l1_a.getB());
  }
  
  public void test_delete_referencee_and_add_it_again() {
    A a = new A(new B("b"));
    
    IObjectId idA = saveAll(a);
    
    // delete the referenced object
    delete(a.getB());
    reopen();
    
    A l1_a = get(idA);
    
    assertNull(l1_a.getB());
  }
  
  public void test_save_and_delete_in_same_transaction() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    
    reopen();
    
    OneInt a_l1 = fObjectStore.get(a_id);
    
    a_l1.setInt(1);
    beginUpdate();
      save(a_l1);
      delete(a_l1);
    endUpdate();
    
    IObjectInfo info = fObjectStore.getObjectInfoForId(a_id);
    assertEquals(Constants.INITIAL_REVISION + 2, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());
    assertTrue(info.isDeleted());
    
  }
  
  public void test_save_object_after_deletion() {
    Object object = new Object();
    assertFalse(fObjectStore.contains(object));
    
    fObjectStore.deleteAll(object);
    fObjectStore.delete(object);
    
    assertFalse(fObjectStore.contains(object));
    
    IObjectId id = save(object);
    
    assertTrue(fObjectStore.contains(object));
    assertTrue(fObjectStore.containsId(id));
  }
  
  public void test_save_twice_and_delete_in_same_transaction() {
    Object obj = new Object();
    
    beginUpdate();
      IObjectId id = save(obj);
      save(obj); // update
      delete(obj); // delete must nihilate update
    endUpdate();

    assertFalse(fObjectStore.contains(obj));
    assertFalse(fObjectStore.containsId(id));
    
    reopen();

    assertFalse(fObjectStore.contains(obj));
    assertFalse(fObjectStore.containsId(id));
  }
  
  public void test_update_and_save_in_same_transaction() {
    Object obj = new Object();
    IObjectId id = save(obj);
    
    beginUpdate();
      save(obj); // update
      delete(obj); // delete must nihilate update
      assertFalse(fObjectStore.contains(obj));
      assertFalse(fObjectStore.containsId(id));
    endUpdate();
    
    assertFalse(fObjectStore.contains(obj));
    assertFalse(fObjectStore.containsId(id));
    
    reopen();

    assertFalse(fObjectStore.contains(obj));
    assertFalse(fObjectStore.containsId(id));
  }
  
}
