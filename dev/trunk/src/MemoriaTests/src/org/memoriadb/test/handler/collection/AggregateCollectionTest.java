/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.test.handler.collection;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.CollectionOwner;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class AggregateCollectionTest extends AbstractMemoriaTest {
  
  public void test_aggregated_collection_is_deleted() {
    CollectionOwner co = new CollectionOwner();
    IObjectId id = saveAll(co);
    
    delete(co.getMap());
    delete(co.getList());
    
    reopen();
    
    co = get(id);
    
    assertNull(co.getList());
    assertNull(co.getMap());
  }
  
  public void test_collections_are_null() {
    CollectionOwner co = new CollectionOwner(null, null);
    IObjectId id = saveAll(co);
    
    reopen();
    
    co = get(id);
    
    assertNull(co.getList());
    assertNull(co.getMap());
  }
  
//  public void test_save_shared_collection() {
//    CollectionOwner co1 = new CollectionOwner();
//    CollectionOwner co2 = new CollectionOwner(co1.getMap(), co1.getList());
//    
//    beginUpdate();
//      IObjectId id1 = saveAll(co1);
//      saveAll(co2);
//      deleteAll(co2); // also deletes shared collections!
//    endUpdate();
//    
//    reopen();
//    
//    co1 = get(id1);
//    
//    assertNull(co1.getList());
//    assertNull(co1.getMap());
//  }
  
}

