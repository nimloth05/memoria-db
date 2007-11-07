package org.memoriadb.core.load;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.exception.MemoriaException;

/**
 * 
 * fHydratedObject == null means that this object has been deleted.
 * 
 * @author msc
 */
public class HydratedInfo {
  
  private final IObjectId fId;
  private HydratedObject fHydratedObject;
  private long fVersion;
  private int fOldGenerationCount;
  
  public HydratedInfo(IObjectId id, HydratedObject hydratedObject, long version) {
    fId = id;
    fHydratedObject = hydratedObject;
    fVersion = version;
    fOldGenerationCount = 0;
  }

  public IObjectId getMemoriaClassId() {
    if (isDeleted()) throw new MemoriaException("This Object is deleted");
    return fHydratedObject.getTypeId();
  }

  /**
   * @return the dehydrated object or null, if the object has been deleted.
   * @throws Exception 
   */
  public Object getObject(IReaderContext context) throws Exception {
    if(fHydratedObject == null) return null;
    return fHydratedObject.dehydrate(context);
  }

  public IObjectId getObjectId() {
    return fId;
  }

  public int getOldGenerationCount() {
    return fOldGenerationCount;
  }
  
  public long getVersion() {
    return fVersion;
  }

  public boolean isDeleted() {
    return fHydratedObject == null;
  }

  public void setDeleted() {
    fHydratedObject = null;
  }

  @Override
  public String toString() {
    return "HydratedObject: " + fId.toString() + (isDeleted()?" deleted":"");
  }
  
  /**
   * @param hydratedObject pass null to mark this object as deleted
   */
  public void update(HydratedObject hydratedObject, long version) {
    // es kann nicht 2 mal dieselbe Version abgespeichert sein
    if(fVersion == version) throw new MemoriaException("internal error: object-version " + version);
    
    ++fOldGenerationCount;
    
    // if the current version is bigger than the read one, do nothing.
    if(fVersion > version) return;
    
    fVersion = version;
    fHydratedObject = hydratedObject;
  }
  
}
