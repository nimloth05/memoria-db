package org.memoriadb.core.file.read;

import java.io.*;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

/**
 * Stores all information needed to dehydrate the object. References to other entities are
 * not bound during the dehydration-process. 
 * 
 * @author msc
 *
 */
public class HydratedObject {
  
  private final IObjectId fTypeId;
  private final byte[] fData;
  
  public HydratedObject(IObjectId typeId, byte[] data) {
    fTypeId = typeId;
    fData = data;
  }

  public Object dehydrate(IReaderContext context) throws Exception {
    IMemoriaClass classObject = (IMemoriaClass) context.getExistingObject(fTypeId);
    if (classObject == null) throw new MemoriaException("ClassObject for typeId not found: " + fTypeId);
    
    return instantiate(context, classObject);
  }

  public IObjectId getTypeId() {
    return fTypeId;
  }

  @Override
  public String toString() {
    return "hydrated for type " + fTypeId;
  }
  
  private Object instantiate(IReaderContext context, IMemoriaClass classObject) throws Exception {
    DataInputStream input = new DataInputStream(new ByteArrayInputStream(fData));
    Object deserializedObject = classObject.getHandler().deserialize(input, context, fTypeId);
    if (context.isInDataMode() && !(deserializedObject instanceof IDataObject)) throw new MemoriaException("IHandler must return a IDataObject in DBMode.data. Handler for " + classObject.getJavaClassName() + " returned " + deserializedObject); 
    return deserializedObject;
  }
  
}
