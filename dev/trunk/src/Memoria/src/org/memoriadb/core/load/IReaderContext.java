package org.memoriadb.core.load;

import java.io.*;

import org.memoriadb.core.IDefaultInstantiator;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.exception.MemoriaException;


public interface IReaderContext {

  public IObjectId getArrayMemoriaClass();

  public IDefaultInstantiator getDefaultInstantiator();
  
  /**
   * @return the object for the given <tt>id</tt>
   * throw new {@link MemoriaException} if the given <tt>id</tt> is not found. 
   */
  public Object getExistingObject(IObjectId id);
  
  /**
   * @return the object for the given <tt>id</tt> or null.
   */
  public Object getObject(IObjectId id);

  public IObjectId getPrimitiveClassId();
  
  public boolean isInDataMode();

  public boolean isNullReference(IObjectId id);
  
  public boolean isRootClassId(IObjectId superClassId);
  
  public void objectToBind(IBindable bindable);

  public IObjectId readObjectId(DataInput input) throws IOException;

}
