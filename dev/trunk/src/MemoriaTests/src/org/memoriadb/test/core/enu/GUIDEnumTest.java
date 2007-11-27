package org.memoriadb.test.core.enu;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.id.guid.GuidIdFactory;

public class GUIDEnumTest extends AbstractEnumTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GuidIdFactory.class.getName());
  }


}
