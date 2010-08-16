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

package org.memoriadb.test.core;

import org.memoriadb.CreateConfig;
import org.memoriadb.id.loong.LongId;
import org.memoriadb.id.loong.LongIdFactory;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class LongIdFactoryTest extends AbstractMemoriaTest {
  
  public void test_call_peekNextId_multiple_times() {
    LongId id = (LongId) save(new Object());
    LongIdFactory factory = (LongIdFactory)fObjectStore.getIdFactory();
    
    LongId nextId = factory.peekNexId();
    assertEquals(nextId.getValue(), id.getValue() + 1);
    assertEquals(nextId, factory.peekNexId());
    assertEquals(nextId, factory.peekNexId());
    
    LongId id2 = (LongId) save(new Object());
    assertEquals(nextId, id2);
  }
  
  /**
   * Because of a bug in the id-factory-handling, the ids of deleted objects were not 
   * registered in the IdFactory what lead to redundant ids!
   * 
   */
  public void test_id_factory() {
    LongId id1 = (LongId) save(new Object());
    delete(get(id1));
    reopen();
    LongId id2 = (LongId) save(new Object());
    assertEquals(id1.getValue() + 1, id2.getValue());
  }
  
  public void test_peek_nex_id() {
    LongId id = (LongId) save(new Object());
    LongIdFactory factory = (LongIdFactory)fObjectStore.getIdFactory();
    
    LongId nextId = factory.peekNexId();
    assertEquals(nextId.getValue(), id.getValue() + 1);
    
    LongId id2 = (LongId) save(new Object());
    assertEquals(nextId, id2);
  }
  
  @Override
  protected void configureOpen(CreateConfig config) {
    configure(config);
  }

  @Override
  protected void configureReopen(CreateConfig config) {
    configure(config);
  }

  private void configure(CreateConfig config) {
    config.setIdFactoryName(LongIdFactory.class.getName());
  }
  
}
