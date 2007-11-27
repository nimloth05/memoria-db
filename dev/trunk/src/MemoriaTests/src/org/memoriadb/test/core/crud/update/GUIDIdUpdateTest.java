package org.memoriadb.test.core.crud.update;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.id.guid.GuidIdFactory;

public class GUIDIdUpdateTest extends UpdateTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GuidIdFactory.class.getName());
  }

}
