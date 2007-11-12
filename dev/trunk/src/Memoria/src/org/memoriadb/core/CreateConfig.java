package org.memoriadb.core;

import org.memoriadb.core.block.IBlockManager;
import org.memoriadb.core.id.def.LongIdFactory;


public class CreateConfig extends OpenConfig {

  private final String fIdFactoryClassName;
  private final String fDefaultInstantiatorClassName;

  public CreateConfig() {
    super();
    fIdFactoryClassName = LongIdFactory.class.getName();
    fDefaultInstantiatorClassName = DefaultDefaultInstantiator.class.getName();
  }
  
  public CreateConfig(IBlockManager blockManager, String IdFactoryClassName, String defaultInstantiatorClassName) {
    super(blockManager, DBMode.clazz);
    fIdFactoryClassName = IdFactoryClassName;
    fDefaultInstantiatorClassName = defaultInstantiatorClassName;
  }

  public String getDefaultInstantiatorClassName() {
    return fDefaultInstantiatorClassName;
  }

  public String getIdFactoryClassName() {
    return fIdFactoryClassName;
  }

}
