package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.ISerializeHandler;

public class DefaultHandler implements ISerializeHandler {

  private final MetaClass fClassObject;

  public DefaultHandler(MetaClass classObject) {
    fClassObject = classObject;
  }

  @Override
  public Object desrialize(DataInputStream input, IReaderContext context) throws IOException {
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

}
