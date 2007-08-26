package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;

public class DefaultHandler implements ISerializeHandler {

  private final MetaClass fClassObject;

  public DefaultHandler(MetaClass classObject) {
    fClassObject = classObject;
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context) throws IOException {
    Object result = fClassObject.newInstance();
    
    MetaClass metaObject = fClassObject;
    while(input.available() > 0) {
      
      for(int i = 0; i < metaObject.getFieldCount(); ++i) {
        int fieldId = input.readInt();
        MetaField field = metaObject.getField(fieldId);
        field.getFieldType().readValue(input, result, field.getJavaField(), context);
      }
      
      metaObject = (MetaClass) metaObject.getSuperClass();
    }
    
    return result;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    MetaClass metaObject = fClassObject;
    while (metaObject != null) {
      
      for(MetaField metaField: metaObject.getFields()) {
        output.writeInt(metaField.getId());
        metaField.getFieldType().writeValue(output, obj, metaField.getJavaField(), context);
      }
      
      metaObject = (MetaClass) metaObject.getSuperClass();
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    //FIXME: Vererbung berücksichtigen
    for(MetaField field: fClassObject.getFields()) {
      if(field.getFieldType() != FieldType.clazz) continue;
      
      try {
        // access the field via refelcion
        traversal.handle(field.getJavaField().get(obj));
      }
      catch (Exception e) {
        throw new MemoriaException(e);
      }
    }
  }

}
