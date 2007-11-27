package org.memoriadb.core.mode;

import java.util.Set;

import org.memoriadb.IDataStore;
import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.core.file.Header;
import org.memoriadb.core.id.*;

/**
 * Extended functionality for test-purposes. 
 * 
 * Attention: It may be possible to corrupt the db when misusing the power of this interface.
 * 
 * @author msc
 */
public interface IDataStoreExt extends IDataStore {

  public void checkIndexConsistancy();
  
  public IBlockManager getBlockManager();

  public Header getHeader();  
  
  public IDefaultIdProvider getIdFactory();

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
