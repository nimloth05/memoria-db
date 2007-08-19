package org.memoriadb.core.load;

public class HydratedInfo {
  
  private final long fId;
  private final HydratedObject fHydratedObject;
  private final int fVersion;
  
  public HydratedInfo(long id, HydratedObject hydratedObject, int version) {
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

  public int getVersion() {
    return fVersion;
  }
  
}
