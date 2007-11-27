package org.memoriadb;

import org.memoriadb.handler.array.IArray;
import org.memoriadb.handler.enu.IEnumObject;
import org.memoriadb.handler.field.IFieldbasedObject;


public interface IRefactor {

  /**
   * Creates 
   * @param object TODO
   * @return
   */
  public IFieldbasedObject asFieldDataObject(Object object);
  
  /**
   * 
   * @param klass Array-type: int[].class, int[][].class, etc.
   * @param length
   * @return
   */
  public IArray createArray(Class<?> klass, int length);

  public IArray createArray(String componentType, int dimension, int length);

  /**
   * @return the wrappend enum-object, if it is found in the repo. else, a new EnumDataObject
   * is created which is not yet saved.
   */
  public IEnumObject getEnum(String name, int i);

}
