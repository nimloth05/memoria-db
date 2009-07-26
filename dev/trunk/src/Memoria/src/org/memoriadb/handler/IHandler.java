package org.memoriadb.handler;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.io.DataInputStream;
import java.io.DataOutput;

/**
 * 
 * Knows how to serialize/deserialize objects of a particular type.
 * 
 * @author sandro
 *
 */
public interface IHandler {
  
  /**
   * @param className the name of the java class
   * @param instantiator
   */
  public void checkCanInstantiateObject(String className, IInstantiator instantiator);

  /**
   * @param
   * @param context reader context Object for deserialize.
   * @param typeId the typeId of the object
   * @return the new object
   */
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception;
  
  /**
   * @return Name of the java-type this handler can deal with.
   */
  public String getClassName();

  /**
   * @param obj - object to serialize
   * @param output - the stream
   * @param context 
   */
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception;
  
  
  /**
   * Sends all direct children to the given traversal. For Arrays, or Lists, all contained elements are visited. 
   * For non-Containers, all referenced objects are visited
   */
  public void traverseChildren(Object obj, IObjectTraversal traversal);
  
}
