package org.memoriadb.test.core.crud.update;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.id.loong.LongIdFactory;

public class LongIdUpdateTest extends UpdateTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(LongIdFactory.class.getName());
  }

}
