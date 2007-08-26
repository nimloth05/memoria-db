package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.load.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;

public class MetaFieldClassHandler implements ISerializeHandler {

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context) throws IOException {
    String className = input.readUTF();
    
    MetaClass classObject = new MetaClass(className);
    
    long superClassId = input.readLong();
    if (superClassId != IMetaClass.NO_SUPER_CLASS_ID) context.objectToBind(new ClassInheritanceBinder(classObject, superClassId)); 
    
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
    
    long superClassId = IMetaClass.NO_SUPER_CLASS_ID;
    if (classObject.getSuperClass() != null) superClassId = context.getObjectId(classObject.getSuperClass());
    output.writeLong(superClassId);
    
    for(MetaField field: classObject.getFields()) {
      output.writeInt(field.getId());
      output.writeUTF(field.getName());
      output.writeInt(field.getType());
    }
  }
  

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    throw new MemoriaException("has no children");
  }


}
