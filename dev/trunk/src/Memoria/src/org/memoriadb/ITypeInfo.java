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
   * @return The MemoriaClass for the given <tt>clazz</tt> or null.
   */
  public IMemoriaClass getMemoriaClass(Class<?> clazz);
  
  /**
   * @return The MemoriaClass for the given <tt>obj</tt> or null.
   */
  public IMemoriaClass getMemoriaClass(Object object);

  /**
   * @return The MemoriaClass for the given <tt>className</tt> or null.
   */
  public IMemoriaClass getMemoriaClass(String className);
  
  public IObjectId getMemoriaClassId(Class<?> clazz);
  
  public IObjectId getMemoriaClassId(Object object);
  
  public IObjectId getMemoriaClassId(String className);
}
