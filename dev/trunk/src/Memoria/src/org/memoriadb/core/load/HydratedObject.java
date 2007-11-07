package org.memoriadb.core.load;

import java.io.DataInputStream;
import java.util.*;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.binder.ObjectFieldReference;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.exception.MemoriaException;

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
  
  private final List<ObjectFieldReference> fObjectsToBind = new ArrayList<ObjectFieldReference>();
  
  public HydratedObject(IObjectId typeId, DataInputStream input) {
    fTypeId = typeId;
    fInput = input;
  }

  public Object dehydrate(IReaderContext context) throws Exception {
    IMemoriaClass classObject = (IMemoriaClass) context.getObjectById(fTypeId);
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
    return classObject.getHandler().deserialize(fInput, context);
  }
  
}
