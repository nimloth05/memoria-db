package org.memoriadb.core;

import java.util.*;

import org.memoriadb.block.IBlockManager;
import org.memoriadb.id.loong.LongIdFactory;
import org.memoriadb.instantiator.DefaultInstantiator;

public class CreateConfig extends OpenConfig {

  private String fIdFactoryClassName;

  private final String fDefaultInstantiatorClassName;
  private final List<String> fCustomHandlers = new ArrayList<String>();

  public CreateConfig() {
    super();
    fIdFactoryClassName = LongIdFactory.class.getName();
    fDefaultInstantiatorClassName = DefaultInstantiator.class.getName();
  }

  public CreateConfig(IBlockManager blockManager, String IdFactoryClassName, String defaultInstantiatorClassName) {
    super(blockManager);
    fIdFactoryClassName = IdFactoryClassName;
    fDefaultInstantiatorClassName = defaultInstantiatorClassName;
  }

  public CreateConfig(String idFactoryClassName, String defaultInstantiatorClassName) {
    super();
    fIdFactoryClassName = idFactoryClassName;
    fDefaultInstantiatorClassName = defaultInstantiatorClassName;
  }

  /**
   * @param handlerClass
   *          The IHandler subclass.
   * @param objectClass
   *          java-type which is handled by the given handler.
   */
  public void addCustomHandler(String className) {
    fCustomHandlers.add(className);
  }

  public Iterable<String> getCustomHandlers() {
    return Collections.unmodifiableCollection(fCustomHandlers);
  }

  public String getDefaultInstantiatorClassName() {
    return fDefaultInstantiatorClassName;
  }
  
  public String getIdFactoryClassName() {
    return fIdFactoryClassName;
  }

  public void setIdFactoryName(String name) {
    fIdFactoryClassName = name;
  }

}
