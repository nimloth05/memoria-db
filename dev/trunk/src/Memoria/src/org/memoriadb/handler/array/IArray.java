package org.memoriadb.handler.array;

import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.handler.IDataObject;

/**
 * Represents the array in a mode-independant way.
 * 
 * @author msc
 *
 */
public interface IArray extends IDataObject {
  
  /**
   * @return the value at the given <tt>index</tt>-position
   */
  public Object get(int index);

  public ArrayTypeInfo getTypeInfo();

  /**
   * @return the array-object, depending on the mode either a java-Array or an {@link IArrayDataObject}.
   */
  public Object getResult();
  
  public int length();
  
  /**
   * Sets a value
   */
  public void set(int index, Object value);
  
}
