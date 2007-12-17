package org.memoriadb.test.handler.enu;

import org.memoriadb.CreateConfig;
import org.memoriadb.id.guid.GuidIdFactory;

public class GUIDEnumTest extends AbstractEnumTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GuidIdFactory.class.getName());
  }


}
