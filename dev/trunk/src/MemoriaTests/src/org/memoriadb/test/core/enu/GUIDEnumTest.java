package org.memoriadb.test.core.enu;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.id.guid.GUIDIdFactory;

public class GUIDEnumTest extends AbstractEnumTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GUIDIdFactory.class.getName());
  }


}
