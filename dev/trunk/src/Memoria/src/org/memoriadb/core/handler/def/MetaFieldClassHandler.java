package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.facade.nternal.IObjectTraversal;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.exception.MemoriaException;

public class MetaFieldClassHandler implements ISerializeHandler {

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context) throws IOException {
    String className = input.readUTF();
    MetaClass classObject = new MetaClass(className);
    
    while (input.available() > 0) {
      int fieldId = input.readInt();
      String name = input.readUTF();
      int type = input.readInt();
      MetaField metaField = new MetaField(fieldId, name, type);
      classObject.addMetaField(metaField);
    }
    return classObject;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws IOException {
    MetaClass classObject = (MetaClass) obj;
    output.writeUTF(classObject.getClassName());
    
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
