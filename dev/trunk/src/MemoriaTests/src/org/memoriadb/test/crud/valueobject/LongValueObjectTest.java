package org.memoriadb.test.crud.valueobject;

import org.memoriadb.CreateConfig;
import org.memoriadb.id.loong.LongIdFactory;

public class LongValueObjectTest extends AbstractValueObjectTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(LongIdFactory.class.getName());
  }

}
