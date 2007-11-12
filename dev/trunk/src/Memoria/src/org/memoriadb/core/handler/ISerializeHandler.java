package org.memoriadb.core.handler;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;

public interface ISerializeHandler {

  /**
   * 
   * @param className the name of the java class
   * @param defaultInstantiator
   */
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator);
  
  /**
   * 
   * @param input
   * @param context TODO
   * @param typeId TODO
   * @return the new object
   */
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception;

  /**
   * 
   * @param obj - object to serialize
   * @param output - the stream
   * @param context TODO
   */
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception;
  //FIXME: Diese beiden Methoden müssen von der Schnittstelle entfernt werden, da sie nur der DefaultHandler benötigt.
  public void superDeserialize(Object result, DataInputStream input, IReaderContext context) throws IOException;

  public void superSerialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception;

  /**
   * Sends all direct children to the given traversal. For Arrays, or Lists, all contained elements are visited. 
   * For non-Containers, all referenced objects are visited
   */
  public void traverseChildren(Object obj, IObjectTraversal traversal);
  
}
