package bootstrap.core;

import java.io.DataInputStream;

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
  
  public HydratedObject(long typeId, long objectId, DataInputStream input) {
    fTypeId = typeId;
    fObjectId = objectId;
    fInput = input;
  }
  
  @Override
  public String toString() {
    return "typeId:" + fTypeId + " objectId:" + fObjectId;
  }

  public long getObjectId() {
    return fObjectId;
  }

  public long getTypeId() {
    return fTypeId;
  }
  
  
  
}
