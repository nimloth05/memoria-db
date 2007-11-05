package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;

public class DefaultHandler implements ISerializeHandler {

  private final MemoriaFieldClass fClassObject;

  public DefaultHandler(MemoriaFieldClass classObject) {
    fClassObject = classObject;
  }

  @Override
  public Object deserialize(final DataInputStream input, final IReaderContext context) throws IOException {
    final Object result = fClassObject.newInstance();
    
    superDeserialize(result, input, context);
    if (input.available() > 0) throw new MemoriaException("Object not fully deserialized: " + result);
    
    return result;
  }

  @Override
  public void serialize(final Object obj, final DataOutputStream output, final ISerializeContext context) throws Exception {
    superSerialize(obj, output, context);
  }

  @Override
  public void superDeserialize(Object result, DataInputStream input, IReaderContext context) throws IOException {
    for(int i = 0; i < (fClassObject).getFieldCount(); ++i) {
      int fieldId = input.readInt();
      MemoriaField field = (fClassObject).getField(fieldId);
      field.getFieldType().readFieldValue(input, result, field.getJavaField(), context);
    }
    
    if (fClassObject.getSuperClass() == null) return;
    fClassObject.getSuperClass().getHandler().superDeserialize(result, input, context);
  }

  @Override
  public void superSerialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    for(MemoriaField metaField: (fClassObject).getFields()) {
      output.writeInt(metaField.getId());
      metaField.getFieldType().writeFieldValue(output, obj, metaField.getJavaField(), context);
    }
    if (fClassObject.getSuperClass() == null) return;
    fClassObject.getSuperClass().getHandler().superSerialize(obj, output, context);
  }

  @Override
  public void traverseChildren(final Object obj, final IObjectTraversal traversal) {
    new MetaClassInheritanceTraverser(fClassObject) {

      @Override
      protected void handle(IMemoriaClass metaObject) {
        for(MemoriaField field: ((MemoriaFieldClass) metaObject).getFields()) {
          if(field.getFieldType() != Type.typeClass) continue;
          
          try {
            // access the field via refelcion
            traversal.handle(field.getJavaField().get(obj));
          }
          catch (Exception e) {
            throw new MemoriaException("Exception during object traversel. Java Class: '"+metaObject.getJavaClass()+"' Java-Field: '"+field+"' type of the field: '"+field.getFieldType()+"'", e);
          }
        }
      }
    };
  }

}
