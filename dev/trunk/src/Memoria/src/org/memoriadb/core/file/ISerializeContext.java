package org.memoriadb.core.file;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.exception.MemoriaException;

public interface ISerializeContext {
  
  public boolean contains(Object obj);

  /**
   * @return The if for the given <tt>obj</tt>
   * @throw {@link MemoriaException} if the given <tt>obj</tt> is not found. 
   */
  public IObjectId getExistingtId(Object obj);
  
  /**
   * @return ObjectId of the MemoriaClass representing the given java-class
   * @throws MemoriaException if no class is found.
   */
  public IObjectId getMemoriaClassId(String javaClassName);

  public IObjectId getNullReference();
  
  public IObjectId getRootClassId();

}
