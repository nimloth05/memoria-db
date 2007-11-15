package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.MemoriaHandlerClass;
import org.memoriadb.exception.*;

public class HandlerClassHandler implements ISerializeHandler {

  @Override
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator) {
    if (!MemoriaHandlerClass.class.getName().equals(className)) throw new SchemaCorruptException("I am a handler for type " + MemoriaHandlerClass.class.getName() +" but I was called for " + className);
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
    //String className = input.readUTF();
    String handlerName = input.readUTF();
    try {
      return new MemoriaHandlerClass(handlerName, typeId);
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
    
    //output.writeUTF(classObject.getJavaClassName());
    output.writeUTF(classObject.getHandlerName());
  }

  @Override
  public void superDeserialize(Object result, DataInputStream input, IReaderContext context) throws IOException {
    throw new UnsupportedOperationException("This method is not supported on this handler");
  }

  @Override
  public void superSerialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    throw new UnsupportedOperationException("This method is not supported on this handler");    
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    throw new MemoriaException("has no children");
  }


}
