package org.memoriadb;

import org.memoriadb.handler.array.IArray;
import org.memoriadb.handler.enu.IEnumObject;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.handler.value.LangValueObject;


public interface IRefactor {

  /**
   * Creates 
   * @param object TODO
   * @return
   */
  public IFieldbasedObject asFieldDataObject(Object object);
  
  /**
   * 
   * @param klass type of the array: int[].class, int[][].class, etc.
   * @param length
   * @return
   */
  public IArray createArray(Class<?> klass, int length);

  public IArray createArray(String componentType, int dimension, int length);

  /**
   * @return the wrapped enum-object, if it is found in the repo. else, a new EnumDataObject
   * is created which is not yet saved.
   */
  public IEnumObject getEnum(String className, String name);
  
  public <T> LangValueObject<T> getLangValueObject(T value);

}
