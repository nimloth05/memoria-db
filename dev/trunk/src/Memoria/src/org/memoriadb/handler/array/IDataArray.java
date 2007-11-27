package org.memoriadb.handler.array;

import org.memoriadb.handler.IDataObject;

public interface IDataArray extends IArray {
  
  /**
   * Enlarges the array 
   * @param obj is either primitive or of type {@link IDataObject}.
   */
  public void add(Object obj);
  
}
