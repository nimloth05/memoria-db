package org.memoriadb.core.handler;

import java.io.*;

import org.memoriadb.core.*;

public interface ISerializeHandler {
  
  /**
   * 
   * @param input
   * @param context TODO
   * @return the new object
   */
  public Object desrialize(DataInputStream input, IReaderContext context) throws Exception;
  
  /**
   * 
   * @param obj - object to serialize
   * @param output - the stream
   * @param context TODO
   */
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception;

}
