package org.memoriadb.test.core.enu;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.id.def.LongIdFactory;

public class LongCrudTest extends AbstractEnumTest {

  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(LongIdFactory.class.getName());
  }

}
