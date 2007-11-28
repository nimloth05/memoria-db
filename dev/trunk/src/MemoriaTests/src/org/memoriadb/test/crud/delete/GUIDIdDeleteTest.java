package org.memoriadb.test.crud.delete;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.id.guid.GuidIdFactory;

public class GUIDIdDeleteTest extends DeleteTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GuidIdFactory.class.getName());
  }

}
