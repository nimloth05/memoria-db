package org.memoriadb.test.core.crud.delete;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.id.def.LongIdFactory;

public class LongIdDeleteTest extends DeleteTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(LongIdFactory.class.getName());
  }

}
