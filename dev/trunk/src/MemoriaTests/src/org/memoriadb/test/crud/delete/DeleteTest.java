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

package org.memoriadb.test.crud.delete;

import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.util.Constants;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.crud.testclass.A;
import org.memoriadb.test.testclasses.IntObject;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

public abstract class DeleteTest extends AbstractMemoriaTest {
  
  public void test_add_and_delete_in_same_transaction(){
    
    IntObject a = new IntObject(0);
    beginUpdate();
      IObjectId a_id = save(a);
      delete(a);
    endUpdate();
    
    assertFalse(fObjectStore.contains(a));
    
    reopen();
    
    assertFalse(fObjectStore.containsId(a_id));
  }
  
  public void test_basic_delete() {
    IntObject a = new IntObject(0);
    IObjectId a_id = save(a);
    delete(a);
    
    assertFalse(fObjectStore.containsId(a_id));
    IObjectInfo info = fObjectStore.getObjectInfoForId(a_id);
    assertTrue(info.isDeleted());
    assertEquals(Constants.INITIAL_REVISION + 2, getRevision(info));
    assertEquals(1, info.getOldGenerationCount());
    
    reopen();
    
    assertFalse(fObjectStore.containsId(a_id));
    
    info = fObjectStore.getObjectInfoForId(a_id);
    assertEquals(Constants.INITIAL_REVISION + 2, getRevision(info));
    assertEquals(1, info.getOldGenerationCount());
  }
  
  public void test_delete_aggregate() {
    A a = new A(new StringObject("b"));
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
    assertEquals(Constants.INITIAL_REVISION + 2, getRevision(a_info));
    assertEquals(1, a_info.getOldGenerationCount());

    IObjectInfo b_info = fObjectStore.getObjectInfoForId(b_id);
    assertTrue(b_info.isDeleted());
    assertEquals(Constants.INITIAL_REVISION + 2, getRevision(b_info));
    assertEquals(1, b_info.getOldGenerationCount());

  }
  
  public void test_delete_aggregate_and_add_it_again() {
    A a = new A(new StringObject("b"));
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
    A a = new A(new StringObject("b"));
    
    IObjectId id = saveAll(a);
    
    // delete the referenced object
    delete(a.getB());
    reopen();
    
    A l1_a = get(id);
    
    assertNull(l1_a.getB());
  }
  
  public void test_delete_referencee_and_add_it_again() {
    A a = new A(new StringObject("1"));
    
    IObjectId idA = saveAll(a);
    delete(a.getB());
    reopen();
    
    A l1_a = get(idA);
    assertNull(l1_a.getB());
    StringObject referencee = new StringObject("2");
    l1_a.setB(referencee);
    saveAll(l1_a);
    reopen();

    A l2_a = get(idA);
    assertNotNull(l2_a.getB());
    assertEquals(referencee, l2_a.getB());
  }
  
  public void test_save_and_delete_in_same_transaction() {
    IntObject a = new IntObject(0);
    IObjectId a_id = save(a);
    
    reopen();
    
    IntObject a_l1 = fObjectStore.get(a_id);
    
    a_l1.setInt(1);
    beginUpdate();
      save(a_l1);
      delete(a_l1);
    endUpdate();
    
    IObjectInfo info = fObjectStore.getObjectInfoForId(a_id);
    assertEquals(Constants.INITIAL_REVISION + 2, getRevision(info));
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
  
  public void test_save_object_after_deletion_gets_new_id() {
    Object obj = new Object();
    IObjectId id = save(obj);
    
    assertTrue(fObjectStore.contains(obj));
    assertTrue(fObjectStore.containsId(id));
    
    delete(obj);
    
    assertFalse(fObjectStore.contains(obj));
    assertFalse(fObjectStore.containsId(id));

    IObjectId id2 = save(obj);
    assertFalse(id2.equals(id));
    assertTrue(fObjectStore.contains(obj));
    assertTrue(fObjectStore.containsId(id2));
    assertFalse(fObjectStore.containsId(id));
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
