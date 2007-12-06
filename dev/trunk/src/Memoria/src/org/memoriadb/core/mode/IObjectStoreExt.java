package org.memoriadb.core.mode;


import org.memoriadb.IObjectStore;
import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.SurvivorAgent;
import org.memoriadb.core.file.Header;
import org.memoriadb.id.*;

/**
 * Extended functionality for test-purposes. 
 * 
 * Attention: It may be possible to corrupt the db when misusing the power of this interface.
 * 
 * @author msc
 */
public interface IObjectStoreExt extends IObjectStore {

  public void checkIndexConsistancy();
  
  public IBlockManager getBlockManager();

  public Header getHeader();  
  
  public IIdProvider getIdFactory();

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

  public SurvivorAgent getSurvivorAgent(Block block);
}
