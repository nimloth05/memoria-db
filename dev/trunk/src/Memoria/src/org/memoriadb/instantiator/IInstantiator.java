package org.memoriadb.instantiator;


/**
 * Instantiates objects which are saved by the FieldbasedObjectHandler.
 */
public interface IInstantiator {
  
  /**
   * 
   * @param className the name of the Java Class for the object.
   * @throws Exception TODO
   */
  public void checkCanInstantiateObject(String className) throws CannotInstantiateException;

  /**
   * Instantiate a new object.
   * @param <T>
   * @param className
   * @return
   */
  public <T> T newInstance(String className); 

}
