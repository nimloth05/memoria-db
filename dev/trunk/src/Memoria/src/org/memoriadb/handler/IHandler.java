/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.handler;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.io.*;

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
   * @throws Exception
   */
  public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception;
  
  /**
   * @return Name of the java-type this handler can deal with.
   */
  public String getClassName();

  /**
   * @param obj - object to serialize
   * @param output - the stream
   * @param context
   * @throws Exception
   */
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception;
  
  
  /**
   * Sends all direct children to the given traversal. For Arrays, or Lists, all contained elements are visited. 
   * For non-Containers, all referenced objects are visited
   * @param obj
   * @param obj
   * @param traversal
   * @param traversal
   */
  public void traverseChildren(Object obj, IObjectTraversal traversal);
  
}
