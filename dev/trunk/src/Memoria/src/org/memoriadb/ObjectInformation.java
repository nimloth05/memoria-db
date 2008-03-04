package org.memoriadb;

import org.memoriadb.id.IObjectId;

/**
 * This class needs a better Name!
 * @author sascha
 *
 */
public class ObjectInformation {
  
  private final IObjectId fObjectId;
  private final IObjectId fMemoriaClassId;
  private final long fRevision;
  
  public ObjectInformation(IObjectId objectId, IObjectId memoriaClassId, long revision) {
    super();
    fObjectId = objectId;
    fMemoriaClassId = memoriaClassId;
    fRevision = revision;
  }

  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }


  public IObjectId getObjectId() {
    return fObjectId;
  }


  public long getRevision() {
    return fRevision;
  }
  
}
