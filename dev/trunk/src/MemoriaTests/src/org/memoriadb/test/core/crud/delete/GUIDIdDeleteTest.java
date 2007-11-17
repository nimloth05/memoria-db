package org.memoriadb.test.core.crud.delete;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.id.guid.GUIDIdFactory;

public class GUIDIdDeleteTest extends DeleteTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GUIDIdFactory.class.getName());
  }

}
