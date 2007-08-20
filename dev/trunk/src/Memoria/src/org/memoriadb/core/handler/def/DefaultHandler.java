package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.facade.nternal.IObjectTraversal;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.exception.MemoriaException;

public class DefaultHandler implements ISerializeHandler {

  private final MetaClass fClassObject;

  public DefaultHandler(MetaClass classObject) {
    fClassObject = classObject;
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context) throws IOException {
    Object result = fClassObject.newInstance();
    while(input.available() > 0) {
      int fieldId = input.readInt();
      MetaField field = fClassObject.getField(fieldId);
      field.getFieldType().readValue(input, result, field.getJavaField(result), context);
    }
    return result;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    for(MetaField metaField: fClassObject.getFields()) {
      output.writeInt(metaField.getId());
      metaField.getFieldType().writeValue(output, obj, metaField.getJavaField(obj), context);
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    for(MetaField field: fClassObject.getFields()) {
      if(field.getFieldType() != FieldType.clazz) continue;
      
      try {
        // access the field via refelcion
        traversal.handle(field.getJavaField(obj).get(obj));
      }
      catch (Exception e) {
        throw new MemoriaException(e);
      }
    }
  }

}
