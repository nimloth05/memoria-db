package org.memoriadb.core.handler.field;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.ClassInheritanceBinder;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.*;

public class FieldClassHandler implements ISerializeHandler {

  @Override
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator) {
    if (!MemoriaFieldClass.class.getName().equals(className)) throw new SchemaException("I am a handler for type " + MemoriaFieldClass.class.getName() +" but I was called for " + className);
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
    String className = input.readUTF();
    
    MemoriaFieldClass classObject = new MemoriaFieldClass(className, typeId);
    
    IObjectId superClassId = context.readObjectId(input);
    if (!context.isRootClassId(superClassId)) context.objectToBind(new ClassInheritanceBinder(classObject, superClassId)); 
    
    while (input.available() > 0) {
      int fieldId = input.readInt();
      String name = input.readUTF();
      int ordinal = input.readInt();
      MemoriaField metaField = new MemoriaField(fieldId, name, Type.values()[ordinal]);
      classObject.addMetaField(metaField);
    }
    return classObject;
  }
  
  @Override
  public String getClassName() {
    return MemoriaFieldClass.class.getName();
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
      output.writeInt(field.getType().ordinal());
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    throw new MemoriaException("has no children");
  }

}
