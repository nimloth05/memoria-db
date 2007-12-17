package org.memoriadb.test.crud.valueobject;

import org.memoriadb.CreateConfig;
import org.memoriadb.id.guid.GuidIdFactory;

public class GUIDValueObjectTest extends AbstractValueObjectTest {
  
  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
    config.setIdFactoryName(GuidIdFactory.class.getName());
  }

}
