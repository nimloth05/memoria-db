package org.memoriadb.test.handler.enu;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.id.loong.LongIdFactory;

public class LongEnumTest extends AbstractEnumTest {

  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(LongIdFactory.class.getName());
  }

}
