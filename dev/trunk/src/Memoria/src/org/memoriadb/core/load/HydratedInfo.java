package org.memoriadb.core.load;

import org.memoriadb.exception.MemoriaException;

public class HydratedInfo {
  
  private final long fId;
  private HydratedObject fHydratedObject;
  private long fVersion;
  
  public HydratedInfo(long id, HydratedObject hydratedObject, long version) {
    fId = id;
    fHydratedObject = hydratedObject;
    fVersion = version;
  }

  public HydratedObject getHydratedObject() {
    return fHydratedObject;
  }

  public long getObjectId() {
    return fId;
  }

  public long getVersion() {
    return fVersion;
  }

  public void update(HydratedObject hydratedObject, long version) {
    // es kann nicht 2 mal dieselbe Version abgespeichert sein
    if(fVersion == version) throw new MemoriaException("internal error: object-version " + version);
    
    // if the current version is bigger than the new one, do nothing.
    if(fVersion > version) return;
    
    fVersion = version;
    fHydratedObject = hydratedObject;
  }
  
}
