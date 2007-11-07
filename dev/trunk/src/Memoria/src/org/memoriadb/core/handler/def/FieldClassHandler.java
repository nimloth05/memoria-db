package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.ClassInheritanceBinder;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;

public class FieldClassHandler implements ISerializeHandler {

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context) throws IOException {
    String className = input.readUTF();
    
    MemoriaFieldClass classObject = new MemoriaFieldClass(className);
    
    IObjectId superClassId = context.createFrom(input);
    if (!context.isRootClassId(superClassId)) context.objectToBind(new ClassInheritanceBinder(classObject, superClassId)); 
    
    while (input.available() > 0) {
      int fieldId = input.readInt();
      String name = input.readUTF();
      int type = input.readInt();
      MemoriaField metaField = new MemoriaField(fieldId, name, type, classObject.getJavaClass());
      classObject.addMetaField(metaField);
    }
    return classObject;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws IOException {
    MemoriaFieldClass classObject = (MemoriaFieldClass) obj;
    
    output.writeUTF(classObject.getClassName());
    
    IObjectId superClassId = context.getRootClassId();
    if (classObject.getSuperClass() != null) superClassId = context.getObjectId(classObject.getSuperClass());
    superClassId.writeTo(output);
    
    for(MemoriaField field: classObject.getFields()) {
      output.writeInt(field.getId());
      output.writeUTF(field.getName());
      output.writeInt(field.getType());
    }
  }
  
  @Override
  public void superDeserialize(Object result, DataInputStream input, IReaderContext context) throws IOException {
    throw new MemoriaException("has no children");
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
