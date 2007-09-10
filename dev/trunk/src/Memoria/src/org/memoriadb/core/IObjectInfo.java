package org.memoriadb.core;

/**
 * Read-only interface. 
 * {@link ObjectInfo}s are part of Memoria's internal data structure and must not be changed! 
 * 
 * @author msc
 *
 */
public interface IObjectInfo {

  public long getId();

  public Object getObj();

  public int getOldGenerationCount();

  public long getVersion();

  public boolean isDeleted();

}
