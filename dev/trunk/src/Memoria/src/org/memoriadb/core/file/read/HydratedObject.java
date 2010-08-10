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

package org.memoriadb.core.file.read;

import java.nio.ByteBuffer;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.io.*;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

/**
 * Stores all information needed to dehydrate the object. References to other entities are
 * not bound during the dehydration-process. 
 * 
 * @author msc
 *
 */
public class HydratedObject {
  
  private final IObjectId fTypeId;
  private final ByteBuffer fData;
  
  public HydratedObject(IObjectId typeId, ByteBuffer data) {
    fTypeId = typeId;
    fData = data;
  }

  public Object dehydrate(IReaderContext context) throws Exception {
    IMemoriaClass classObject = (IMemoriaClass) context.getExistingObject(fTypeId);
    if (classObject == null) throw new MemoriaException("ClassObject for typeId not found: " + fTypeId);
    
    return instantiate(context, classObject);
  }

  public IObjectId getTypeId() {
    return fTypeId;
  }

  @Override
  public String toString() {
    return "hydrated for type " + fTypeId;
  }
  
  private Object instantiate(IReaderContext context, IMemoriaClass classObject) throws Exception {
    fData.rewind();
    IDataInput input = new ByteBufferDataInput(fData);
    Object deserializedObject = classObject.getHandler().deserialize(input, context, fTypeId);
    if (context.isInDataMode() && !(deserializedObject instanceof IDataObject)) throw new MemoriaException("IHandler must return a IDataObject in DBMode.data. Handler for " + classObject.getJavaClassName() + " returned " + deserializedObject); 
    return deserializedObject;
  }
  
}
