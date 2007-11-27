package org.memoriadb.test.core.crud.basic;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.id.loong.LongIdFactory;

public class LongCrudTest extends BasicCrudTest {

  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(LongIdFactory.class.getName());
  }

}
