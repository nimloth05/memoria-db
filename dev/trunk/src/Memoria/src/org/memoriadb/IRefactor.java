package org.memoriadb;

import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.handler.array.IArray;

public interface IRefactor {

  /**
   * changes the content of an array
   * @param index 
   * @param simpleTestObj object of a type which is known to the db.
   */
  public void arraySet(int i, Object obj);
  
  /**
   * 
   * @param klass Array-type: int[].class, int[][].class, etc.
   * @param length
   * @return
   */
  public IArray createArray(Class<?> klass, int length);
  
  public IArray createArray(String componentType, int dimension, int length);

  /**
   * Creates 
   * @return
   */
  public IDataObject createObject();

}
