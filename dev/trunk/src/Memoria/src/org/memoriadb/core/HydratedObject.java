package org.memoriadb.core;

import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.util.*;

import org.memoriadb.core.binder.ObjectFieldReference;
import org.memoriadb.exception.MemoriaException;

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
  
  private final List<ObjectFieldReference> fObjectsToBind = new ArrayList<ObjectFieldReference>();
  
  public HydratedObject(long typeId, long objectId, DataInputStream input) {
    fTypeId = typeId;
    fObjectId = objectId;
    fInput = input;
  }

  public Object dehydrate(IReaderContext context) throws Exception {
    IMetaClass classObject = (IMetaClass) context.getObjectById(fTypeId);
    if (classObject == null) throw new MemoriaException("ClassObject for typeId not found: " + fTypeId);
    
    return instantiate(context, classObject);
  }

  public long getObjectId() {
    return fObjectId;
  }

  public long getTypeId() {
    return fTypeId;
  }

  public void objectToBind(Object object, Field field, long targetId) {
    fObjectsToBind.add(new ObjectFieldReference(object, field, targetId));
  }
  
  @Override
  public String toString() {
    return "typeId:" + fTypeId + " objectId:" + fObjectId;
  }
  
  private Object instantiate(IReaderContext context, IMetaClass classObject) throws Exception {
    return classObject.getHandler().desrialize(fInput, context);
  }
  
}
