package org.memoriadb.core;

import org.memoriadb.core.block.Block;
import org.memoriadb.core.id.IObjectId;

/**
 * Read-only interface. 
 * {@link ObjectInfo}s are part of Memoria's internal data structure and must not be changed! 
 * 
 * @author msc
 *
 */
public interface IObjectInfo {

  public void changeCurrentBlock(Block block);

  public Block getCurrentBlock();

  public IObjectId getId();

  public IObjectId getMemoriaClassId();

  public Object getObject();

  public int getOldGenerationCount();

  public long getRevision();
  
  public boolean isDeleted();

}
