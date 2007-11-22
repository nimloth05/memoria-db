package org.memoriadb.test.core.handler.array;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.id.def.LongIdFactory;

public class LongBasicMultiDimensionalArrayTest extends BasicMultiDimensionalArrayTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(LongIdFactory.class.getName());
  }


}
