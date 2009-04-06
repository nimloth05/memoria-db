package org.memoriadb.test.core;

import org.memoriadb.CreateConfig;
import org.memoriadb.id.loong.*;
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
