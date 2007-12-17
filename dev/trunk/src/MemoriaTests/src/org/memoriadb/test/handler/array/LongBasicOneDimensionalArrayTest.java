package org.memoriadb.test.handler.array;

import org.memoriadb.CreateConfig;
import org.memoriadb.id.loong.LongIdFactory;

public class LongBasicOneDimensionalArrayTest extends BasicOneDimensionalArrayTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(LongIdFactory.class.getName());
  }


}
