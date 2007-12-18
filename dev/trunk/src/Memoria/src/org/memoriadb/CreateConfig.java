package org.memoriadb;

import java.util.*;

import org.memoriadb.block.IBlockManager;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.loong.LongIdFactory;
import org.memoriadb.instantiator.DefaultInstantiator;

public class CreateConfig extends OpenConfig {

  private String fIdFactoryClassName;
  private boolean fUseCompression;

  private final String fDefaultInstantiatorClassName;
  private final List<IHandler> fCustomHandlers = new ArrayList<IHandler>();
  private final List<Class<?>> fValueClasses = new ArrayList<Class<?>>();

  public CreateConfig() {
    super();
    fIdFactoryClassName = LongIdFactory.class.getName();
    fDefaultInstantiatorClassName = DefaultInstantiator.class.getName();
    fUseCompression = true;
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
  public void addCustomHandler(IHandler handler) {
    fCustomHandlers.add(handler);
  }

  /**
   * Declares the given class as value-class (it's objects are inline stored value-objects).
   * @param clazz
   */
  public void addValueClass(Class<?> clazz) {
    fValueClasses.add(clazz);
  }

  public Iterable<IHandler> getCustomHandlers() {
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

  public boolean isUseCompression() {
    return fUseCompression;
  }

  public void setIdFactoryName(String name) {
    fIdFactoryClassName = name;
  }

  public void setUseCompression(boolean useCompression) {
    fUseCompression = useCompression;
  }
  

}
