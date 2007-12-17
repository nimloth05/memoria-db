package org.memoriadb.test.crud.delete;

import org.memoriadb.CreateConfig;
import org.memoriadb.id.loong.LongIdFactory;

public class LongIdDeleteTest extends DeleteTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(LongIdFactory.class.getName());
  }

}
