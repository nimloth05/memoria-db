package org.memoriadb.core;

import java.util.Set;

import org.memoriadb.IObjectStore;
import org.memoriadb.core.block.*;
import org.memoriadb.core.id.IObjectId;

/**
 * Extended functionality for test-purposes. 
 * 
 * Attention: It may be possible to corrupt the db when misusing the power of this interface.
 * 
 * @author msc
 */
public interface IObjectStoreExt extends IObjectStore {

  public void checkSanity();
  
  public IBlockManager getBlockManager();
  
  public int getIdSize();

  /**
   * @return The stored ObjectInfo for the given object or null, if the given obj is unknown or deleted.
   */
  public IObjectInfo getObjectInfo(Object obj);  
  
  /**
   * @return The stored ObjectInfo for the given id or null, if the given id is unknown. This method may work
   * even for deleted objects, if the delete-marker is still present.
   */
  public IObjectInfo getObjectInfoForId(IObjectId id);

  public Set<ObjectInfo> getSurvivors(Block block);
}
