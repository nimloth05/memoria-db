package org.memoriadb.core.load;

import java.io.DataInputStream;

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
  private final DataInputStream fInput;
  
  public HydratedObject(IObjectId typeId, DataInputStream input) {
    fTypeId = typeId;
    fInput = input;
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
    Object deserializedObject = classObject.getHandler().deserialize(fInput, context, fTypeId);
    if (context.isInDataMode() && !(deserializedObject instanceof IDataObject)) throw new MemoriaException("IHandler must return a IDataObject in DBMode.data. Handler for " + classObject.getJavaClassName() + " returned " + deserializedObject); 
    return deserializedObject;
  }
  
}
