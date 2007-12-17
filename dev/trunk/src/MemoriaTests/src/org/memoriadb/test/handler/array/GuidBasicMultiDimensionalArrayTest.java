package org.memoriadb.test.handler.array;

import org.memoriadb.CreateConfig;
import org.memoriadb.id.guid.GuidIdFactory;

public class GuidBasicMultiDimensionalArrayTest extends BasicMultiDimensionalArrayTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GuidIdFactory.class.getName());
  }


}
