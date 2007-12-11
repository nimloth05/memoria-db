package org.memoriadb.handler.field;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class FieldbasedClassHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!FieldbasedMemoriaClass.class.getName().equals(className)) throw new SchemaException("I am a handler for type " + FieldbasedMemoriaClass.class.getName() +" but I was called for " + className);
  }
  
  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
    String className = input.readUTF();
    boolean hasValueObejctAnnotation = input.readBoolean();
    
    FieldbasedMemoriaClass classObject = new FieldbasedMemoriaClass(className, typeId, hasValueObejctAnnotation);
    
    IObjectId superClassId = context.readObjectId(input);
    if (!context.isRootClassId(superClassId)) context.objectToBind(new ClassInheritanceBinding(classObject, superClassId)); 
    
    while (input.available() > 0) {
      int fieldId = input.readInt();
      String name = input.readUTF();
      int ordinal = input.readInt();
      boolean isWeakRef = input.readBoolean();
      MemoriaField metaField = new MemoriaField(fieldId, name, Type.values()[ordinal], isWeakRef);
      classObject.addMetaField(metaField);
    }
    return classObject;
  }

  @Override
  public String getClassName() {
    return FieldbasedMemoriaClass.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws IOException {
    FieldbasedMemoriaClass classObject = (FieldbasedMemoriaClass) obj;
    
    output.writeUTF(classObject.getClassName());
    output.writeBoolean(classObject.hasValueObjectAnnotation());
    
    IObjectId superClassId = context.getRootClassId();
    if (classObject.getSuperClass() != null) superClassId = context.getExistingtId(classObject.getSuperClass());
    superClassId.writeTo(output);
    
    for(MemoriaField field: classObject.getFields()) {
      output.writeInt(field.getId());
      output.writeUTF(field.getName());
      output.writeInt(field.getType().ordinal());
      output.writeBoolean(field.isWeakRef());
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
  }

}
