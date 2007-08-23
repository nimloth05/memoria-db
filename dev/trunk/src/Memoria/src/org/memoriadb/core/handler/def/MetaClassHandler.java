package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.HandlerMetaClass;
import org.memoriadb.exception.MemoriaException;

public class MetaClassHandler implements ISerializeHandler {

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context) throws IOException {
    String className = input.readUTF();
    String handlerName = input.readUTF();
    try {
      return new HandlerMetaClass(handlerName, className);
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws IOException {
    HandlerMetaClass classObejct = (HandlerMetaClass) obj;
    
    output.writeUTF(classObejct.getJavaClassName());
    output.writeUTF(classObejct.getHandlerName());
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    throw new MemoriaException("has no children");
  }


}
