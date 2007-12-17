package org.memoriadb;

import java.util.*;

import org.memoriadb.block.IBlockManager;
import org.memoriadb.id.loong.LongIdFactory;
import org.memoriadb.instantiator.DefaultInstantiator;

public class CreateConfig extends OpenConfig {

  private String fIdFactoryClassName;

  private final String fDefaultInstantiatorClassName;
  private final List<String> fCustomHandlers = new ArrayList<String>();
  private final List<Class<?>> fValueClasses = new ArrayList<Class<?>>();

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

  /**
   * Declares the given class as value-class (it's objects are inline stored value-objects).
   * @param clazz
   */
  public void addValueClass(Class<?> clazz) {
    fValueClasses.add(clazz);
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

  public Iterable<Class<?>> getValueClasses() {
    return fValueClasses;
  }

  public void setIdFactoryName(String name) {
    fIdFactoryClassName = name;
  }
  
  

}
