package org.memoriadb.test.core;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.id.def.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class LongIdFactoryTest extends AbstractObjectStoreTest {
  
  /**
   * Because of a bug in the id-factory-handling, the ids of deleted objects were not 
   * registered in the IdFactory what lead to redundant ids!
   * 
   */
  public void test_id_factory() {
    LongObjectId id1 = (LongObjectId) save(new Object());
    delete(get(id1));
    reopen();
    LongObjectId id2 = (LongObjectId) save(new Object());
    assertEquals(id1.getLong()+1, id2.getLong());
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