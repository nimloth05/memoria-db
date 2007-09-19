package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.ClassInheritanceBinder;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;

public class MetaFieldClassHandler implements ISerializeHandler {

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context) throws IOException {
    String className = input.readUTF();
    
    MetaClass classObject = new MetaClass(className);
    
    long superClassId = input.readLong();
    if (superClassId != IdConstants.NO_SUPER_CLASS) context.objectToBind(new ClassInheritanceBinder(classObject, superClassId)); 
    
    while (input.available() > 0) {
      int fieldId = input.readInt();
      String name = input.readUTF();
      int type = input.readInt();
      MetaField metaField = new MetaField(fieldId, name, type, classObject.getJavaClass());
      classObject.addMetaField(metaField);
    }
    return classObject;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws IOException {
    MetaClass classObject = (MetaClass) obj;
    
    output.writeUTF(classObject.getClassName());
    
    long superClassId = IdConstants.NO_SUPER_CLASS;
    if (classObject.getSuperClass() != null) superClassId = context.getObjectId(classObject.getSuperClass());
    output.writeLong(superClassId);
    
    for(MetaField field: classObject.getFields()) {
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
