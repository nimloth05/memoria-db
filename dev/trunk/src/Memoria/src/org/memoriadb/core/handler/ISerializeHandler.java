package org.memoriadb.core.handler;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.load.IReaderContext;

public interface ISerializeHandler {
  
  /**
   * 
   * @param input
   * @param context TODO
   * @return the new object
   */
  public Object deserialize(DataInputStream input, IReaderContext context) throws Exception;
  
  /**
   * 
   * @param obj - object to serialize
   * @param output - the stream
   * @param context TODO
   */
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception;

  /**
   * Sends all direct children to the given traversal. For Arrays, or Lists, all contained elements are visited. 
   * For non-Containers, all referenced objects are visited
   */
  public void traverseChildren(Object obj, IObjectTraversal traversal);
  
}
