package org.memoriadb.core.file;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;

public interface IWriterContext {
  
  public boolean contains(Object obj);

  /**
   * @return The id for the given <tt>obj</tt>
   * @throw {@link MemoriaException} if the given <tt>obj</tt> is not found. 
   */
  public IObjectId getExistingtId(Object object);
  
  public IMemoriaClass getMemoriaClass(IObjectId id);
  
  /**
   * 
   * @param object
   * @return the memoriaClass for the given object.
   */
  public IMemoriaClass getMemoriaClass(Object object);

  /**
   * @return ObjectId of the MemoriaClass representing the given java-class
   * @throws MemoriaException if no class is found.
   */
  public IObjectId getMemoriaClassId(String javaClassName);
  
  public IObjectId getNullReference();

  public IObjectId getRootClassId();

}
