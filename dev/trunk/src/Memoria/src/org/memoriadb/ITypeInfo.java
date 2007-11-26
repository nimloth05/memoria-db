package org.memoriadb;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;

public interface ITypeInfo {

  /**
   * Adds a MemoriaClass (if not already present) for the given java class.
   * 
   * @return Id if the added are already present memoria class
   */
  public IObjectId addMemoriaClass(Class<?> clazz);

  public IObjectId getMemoriaArrayClass();
  
  /**
   * @return The Class for the given <tt>clazz</tt> or null.
   */
  public IMemoriaClass getMemoriaClass(Class<?> clazz);

  /**
   * @return The Class for the given <tt>obj</tt>.
   */
  public IMemoriaClass getMemoriaClass(Object obj);
  
  public IObjectId getMemoriaClassId(Class<?> clazz);
  
  public IObjectId getMemoriaClassId(Object obj);
}
