package org.memoriadb.core;

/**
 * Instantiates objects which are saved by the DefaultHandler.
 */
public interface IDefaultInstantiator {
  
  /**
   * 
   * @param className the name of the Java Class for the object.
   * @return true if this instantiator can instantiate this kind of object.
   */
  public boolean canInstantiateObject(String className);

  /**
   * Instantiate a new object.
   * @param <T>
   * @param className
   * @return
   */
  public <T> T newInstance(String className); 

}
