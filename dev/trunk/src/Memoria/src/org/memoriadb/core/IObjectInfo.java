package org.memoriadb.core;

import org.memoriadb.core.id.IObjectId;

/**
 * Read-only interface. 
 * {@link ObjectInfo}s are part of Memoria's internal data structure and must not be changed! 
 * 
 * @author msc
 *
 */
public interface IObjectInfo {

  public IObjectId getId();

  public IObjectId getMemoriaClassId();

  public Object getObj();

  public int getOldGenerationCount();

  public long getVersion();
  
  public boolean isDeleted();

}
