package org.memoriadb.test.core.crud.update;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.id.guid.GUIDIdFactory;

public class GUIDIdUpdateTest extends UpdateTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GUIDIdFactory.class.getName());
  }

}
