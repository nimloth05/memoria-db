package org.memoriadb.core.handler.array;

import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.util.ArrayTypeInfo;

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

  public ArrayTypeInfo getComponentTypeInfo();

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
