package org.memoriadb.core.load;

public class HydratedInfo {
  
  private final long fId;
  private final HydratedObject fHydratedObject;
  private final long fVersion;
  
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
  
}
