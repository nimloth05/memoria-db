package org.memoriadb.core.file.read;

import java.io.*;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;


public interface IReaderContext {

  /**
   * Use this method for bindings with NO side-effects  (no method-call during binding): for example fields, arrays.
   */
  public void addGenOneBinding(IBindable bindable);

  /**
   * Use this method for bindings with side-effects (for example call to equals, hashCode): Sets, maps.
   * 
   * The bindings added with this method are added after the bindings added with {@link IReaderContext#addGenOneBinding(IBindable)}.
   */
  public void addGenTwoBinding(IBindable bindable);
  
  public IObjectId getArrayMemoriaClass();
  
  public IInstantiator getDefaultInstantiator();

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

  public IObjectId readObjectId(DataInput input) throws IOException;

}
