package org.memoriadb.test.core.handler.array;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.id.guid.GuidIdFactory;

public class GuidBasicOneiDimensionalArrayTest extends BasicOneDimensionalArrayTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GuidIdFactory.class.getName());
  }


}
