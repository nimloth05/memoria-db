package org.memoriadb.core.mode;

import org.memoriadb.core.*;
import org.memoriadb.core.id.IObjectId;

/**
 * Base-interface for IObjectStore and IDataStore.
 * 
 * @author msc
 * 
 */
public interface IModeStrategy {
  /**
   * @return ObjectId of the MemoriaClass for the given obj
   */
  public abstract IObjectId addMemoriaClassIfNecessary(TrxHandler trxHandler, Object obj);

  /**
   * Before an object is added to the ObjectRepository, it is checked for instantiability.
   */
  public abstract void checkCanInstantiateObject(ITrxHandler trxHandler, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator);

  /**
   * @return true, if the db is operated in data-mode (the java classes are not required).
   */
  public abstract boolean isInDataMode();

}
