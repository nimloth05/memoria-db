package org.memoriadb.core;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Stores all information needed to dehydrate the object. References to other entities are
 * not bound during the dehydration-process. 
 * 
 * @author msc
 *
 */
public class HydratedObject {
  
  private final long fTypeId;
  private final long fObjectId;
  private final DataInputStream fInput;
  
  private final List<ObjectReference> fObjectsToBind = new ArrayList<ObjectReference>();
  
  public HydratedObject(long typeId, long objectId, DataInputStream input) {
    fTypeId = typeId;
    fObjectId = objectId;
    fInput = input;
  }

  public Object dehydrate(IReaderContext context) throws Exception {
    MetaClass classObject = (MetaClass) context.getObjectById(fTypeId);
    
    if (classObject.isJavaSerialization()) {
      MemoriaObjectInputStream in = new MemoriaObjectInputStream(fInput);
      return in.readObject();
    }
    
    return instantiate(context, classObject);
  }

  public long getObjectId() {
    return fObjectId;
  }

  public long getTypeId() {
    return fTypeId;
  }

  public void objectToBind(Object object, Field field, long targetId) {
    fObjectsToBind.add(new ObjectReference(object, field, targetId));
  }
  
  @Override
  public String toString() {
    return "typeId:" + fTypeId + " objectId:" + fObjectId;
  }
  
  private Object instantiate(IReaderContext context, MetaClass classObject) throws IOException {
    Object result = classObject.newInstance();
    while(fInput.available() > 0) {
      int fieldId = fInput.readInt();
      MetaField field = classObject.getField(fieldId);
      field.getFieldType().readValue(fInput, result, field.getJavaField(result), context);
    }
    return result;
  }
  
}
