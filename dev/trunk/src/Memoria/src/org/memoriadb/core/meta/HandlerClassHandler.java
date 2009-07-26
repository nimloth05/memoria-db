package org.memoriadb.core.meta;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.field.ClassInheritanceBinding;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

public class HandlerClassHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!MemoriaClass.class.getName().equals(className)) throw new SchemaException("I am a handler for type " + MemoriaClass.class.getName() +" but I was called for " + className);
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
    String javaClassName = input.readUTF();
    String handlerName = input.readUTF();
    boolean hasValueObjectAnnotation = input.readBoolean();
    IObjectId superClassId = context.readObjectId(input);
    
    IHandler handler = instantiateHandler(handlerName, javaClassName);
    
    MemoriaClass memoriaClass = new MemoriaClass(handler, typeId, hasValueObjectAnnotation);
    
    if (!context.isRootClassId(superClassId)) context.addGenOneBinding(new ClassInheritanceBinding(memoriaClass, superClassId));
      
    return memoriaClass;
  }

  @Override
  public String getClassName() {
    return MemoriaClass.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws IOException {
    MemoriaClass classObject = (MemoriaClass) obj;
    
    output.writeUTF(classObject.getJavaClassName());
    output.writeUTF(classObject.getHandlerName());
    output.writeBoolean(classObject.isValueObject());
    
    IObjectId superClassId = context.getRootClassId();
    if (classObject.getSuperClass() != null) {
      superClassId = context.getExistingtId(classObject.getSuperClass());
    }
    superClassId.writeTo(output);
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {}

  private IHandler instantiateHandler(String handlerName, String javaClassName) {
    return ReflectionUtil.createInstanceWithDefaultOrStringCtor(handlerName, javaClassName);
  }

}