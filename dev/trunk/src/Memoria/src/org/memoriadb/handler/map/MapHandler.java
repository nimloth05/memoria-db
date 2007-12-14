package org.memoriadb.handler.map;

import java.io.*;
import java.util.Map;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.ReflectionUtil;
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
  public Object deserialize(DataInputStream input, final IReaderContext context, IObjectId typeId) throws Exception {
    Map<Object, Object> map = createMap();
    
    while (input.available() > 0) {

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

  private IObjectResolver readNextElement(DataInputStream input, final IReaderContext context) {
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
