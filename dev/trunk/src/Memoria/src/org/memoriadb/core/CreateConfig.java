package org.memoriadb.core;

import org.memoriadb.core.block.IBlockManager;
import org.memoriadb.core.id.def.LongIdFactory;


public class CreateConfig extends OpenConfig {

  private final String fIdFactoryClassName;

  public CreateConfig() {
    super();
    fIdFactoryClassName = LongIdFactory.class.getName();
  }
  
  public CreateConfig(IBlockManager blockManager, String IdFactoryClassName) {
    super(blockManager, DBMode.clazz);
    fIdFactoryClassName = IdFactoryClassName;
  }

  public String getIdFactoryClassName() {
    return fIdFactoryClassName;
  }
  
}
