package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.MemoriaHandlerClass;
import org.memoriadb.exception.*;
import org.memoriadb.util.ReflectionUtil;

public class HandlerClassHandler implements ISerializeHandler {

  @Override
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator) {
    if (!MemoriaHandlerClass.class.getName().equals(className)) throw new SchemaException("I am a handler for type " + MemoriaHandlerClass.class.getName() +" but I was called for " + className);
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
    String javaClassName = input.readUTF();
    String handlerName = input.readUTF();
    try {
      ISerializeHandler handler = instantiateHandler(handlerName, javaClassName);
      return new MemoriaHandlerClass(handler, typeId);
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  @Override
  public String getClassName() {
    return MemoriaHandlerClass.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws IOException {
    MemoriaHandlerClass classObject = (MemoriaHandlerClass) obj;
    
    output.writeUTF(classObject.getJavaClassName());
    output.writeUTF(classObject.getHandlerName());
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    throw new MemoriaException("has no children");
  }

  private ISerializeHandler instantiateHandler(String handlerName, String javaClassName) {
    return ReflectionUtil.createInstanceWithDefaultOrStringCtor(handlerName, javaClassName);
  }


}
