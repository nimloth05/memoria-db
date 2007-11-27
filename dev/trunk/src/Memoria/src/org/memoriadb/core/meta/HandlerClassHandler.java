package org.memoriadb.core.meta;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class HandlerClassHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!HandlerbasedMemoriaClass.class.getName().equals(className)) throw new SchemaException("I am a handler for type " + HandlerbasedMemoriaClass.class.getName() +" but I was called for " + className);
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
    String javaClassName = input.readUTF();
    String handlerName = input.readUTF();
    try {
      IHandler handler = instantiateHandler(handlerName, javaClassName);
      return new HandlerbasedMemoriaClass(handler, typeId);
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  @Override
  public String getClassName() {
    return HandlerbasedMemoriaClass.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws IOException {
    HandlerbasedMemoriaClass classObject = (HandlerbasedMemoriaClass) obj;
    
    output.writeUTF(classObject.getJavaClassName());
    output.writeUTF(classObject.getHandlerName());
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    throw new MemoriaException("has no children");
  }

  private IHandler instantiateHandler(String handlerName, String javaClassName) {
    return ReflectionUtil.createInstanceWithDefaultOrStringCtor(handlerName, javaClassName);
  }


}
