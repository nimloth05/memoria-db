package org.memoriadb.core;

import org.memoriadb.IObjectStore;
import org.memoriadb.core.id.IObjectId;

/**
 * Extended functionality for test-purposes. 
 * 
 *  Attention: It may be possible to corrupt the db when misusing the power of this interface.
 * 
 * @author msc
 */
public interface IObjectStoreExt extends IObjectStore {

  // wird später entfernt, msc...
  public void checkSanity();
  
  public IObjectInfo getObjectInfo(IObjectId id);
  public IObjectInfo getObjectInfo(Object obj);
  
}
