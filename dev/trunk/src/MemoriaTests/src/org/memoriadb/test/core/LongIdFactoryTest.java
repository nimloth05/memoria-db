package org.memoriadb.test.core;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.id.loong.*;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class LongIdFactoryTest extends AbstractMemoriaTest {
  
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
