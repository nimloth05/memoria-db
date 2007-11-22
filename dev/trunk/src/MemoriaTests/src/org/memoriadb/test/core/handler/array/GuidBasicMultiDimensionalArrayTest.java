package org.memoriadb.test.core.handler.array;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.id.guid.GUIDIdFactory;

public class GuidBasicMultiDimensionalArrayTest extends BasicMultiDimensionalArrayTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GUIDIdFactory.class.getName());
  }


}
