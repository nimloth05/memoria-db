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
  public Object deserialize(final DataInputStream input, final IReaderContext context) throws IOException {
    final Object result = fClassObject.newInstance();
    
    while(input.available() > 0) {
      new MetaClassInheritanceTraverser(fClassObject) {

        @Override
        protected void handle(IMetaClass metaObject) throws Exception {
          for(int i = 0; i < ((MetaClass) metaObject).getFieldCount(); ++i) {
            int fieldId = input.readInt();
            MetaField field = ((MetaClass) metaObject).getField(fieldId);
            field.getFieldType().readValue(input, result, field.getJavaField(), context);
          }
        }
      };
    }
    
    return result;
  }

  @Override
  public void serialize(final Object obj, final DataOutputStream output, final ISerializeContext context) throws Exception {
    new MetaClassInheritanceTraverser(fClassObject) {

      @Override
      protected void handle(IMetaClass metaObject) throws Exception {
        for(MetaField metaField: ((MetaClass) metaObject).getFields()) {
          output.writeInt(metaField.getId());
          metaField.getFieldType().writeValue(output, obj, metaField.getJavaField(), context);
        }
      }
      
    };
  }

  @Override
  public void traverseChildren(final Object obj, final IObjectTraversal traversal) {
    new MetaClassInheritanceTraverser(fClassObject) {

      @Override
      protected void handle(IMetaClass metaObject) {
        for(MetaField field: ((MetaClass) metaObject).getFields()) {
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
    };
  }

}
