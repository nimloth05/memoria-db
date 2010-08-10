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

package org.memoriadb.handler.map;

import java.io.DataOutput;
import java.util.Map;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

/**
 * Handles all subclasses of {@link java.util.List}.
 * 
 * @author msc
 * 
 */
public class MapHandler implements IHandler {

  
  private final String fClassName;

  public <T extends Map<?,?>> MapHandler(Class<T> mapClass) {
    this(mapClass.getName());
  }
  
  /**
   * Called from Memoria
   * @param className
   * @param className
   */
  private MapHandler(String className) {
    fClassName = className;
  }

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!getClassName().equals(className)) throw new SchemaException("I am a handler for type " + getClassName() + " but I was called for "
        + className);
  }
  
  @Override
  public Object deserialize(IDataInput input, final IReaderContext context, IObjectId typeId) throws Exception {
    Map<Object, Object> map = createMap();
    
    int size = input.readInt();
    for(int i = 0; i < size; ++i) {

      IObjectResolver key = readNextElement(input, context);
      IObjectResolver value = readNextElement(input, context);

      context.addGenTwoBinding(new MapBindable(map, key, value));
    }

    return context.isInDataMode()? new MapDataObject(map, typeId) : map;
  }
  
  @Override
  public String getClassName() {
    return fClassName;
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    Map<?,?> map = getMapObject(obj);
    
    output.writeInt(map.size());
    for (Map.Entry<?, ?> entry: map.entrySet()) {
      writeListEntry(entry.getKey(), output, context);
      writeListEntry(entry.getValue(), output, context);
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    Map<?,?> map = getMapObject(obj);
    traverse(map.keySet(), traversal);
    traverse(map.values(), traversal);
  }

  protected Map<Object, Object> createMap() {
    try {
      return ReflectionUtil.createInstance(getClassName());
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  private Map<?,?> getMapObject(Object obj) {
    if (obj instanceof IMapDataObject) return ((IMapDataObject) obj).getMap();
    return (Map<?,?>) obj;
  }

  private IObjectResolver readNextElement(IDataInput input, final IReaderContext context) {
    return Type.readValueWithType(input, context, new ITypeVisitor() {

      private IObjectResolver fResult;

      @Override
      public void visitClass(Type type, IObjectId objectId) {
        fResult = new ReferenceResolver(objectId);
      }

      @Override
      public void visitNull() {
        fResult = new PrimitiveResolver(null);        
      }

      @Override
      public void visitPrimitive(Type type, Object value) {
        fResult = new PrimitiveResolver(value);
      }

      @Override
      public void visitValueObject(Object value) {
        fResult = new PrimitiveResolver(value);
      }
      
    }).fResult;
  }

  private void traverse(Iterable<?> iterable, IObjectTraversal traversal) {
    for (Object element : iterable) {
      if (element == null) continue;
      if (!Type.getType(element).isPrimitive()) traversal.handle(element);
    }
  }

  private void writeListEntry(Object listEntry, DataOutput output, IWriterContext context) throws Exception {
    Type.writeValueWithType(output, listEntry, context);

  }

}
