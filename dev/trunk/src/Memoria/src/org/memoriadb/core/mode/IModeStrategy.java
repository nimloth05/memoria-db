package org.memoriadb.core.mode;

import org.memoriadb.core.*;
import org.memoriadb.core.id.IObjectId;

/**
 * @author msc
 * 
 */
public interface IModeStrategy {
  /**
   * @return ObjectId of the MemoriaClass for the given obj
   */
  public abstract IObjectId addMemoriaClassIfNecessary(TransactionHandler transactionHandler, Object obj);

  /**
   * Before an object is added to the ObjectRepository, it is checked for instantiability.
   */
  public abstract void checkCanInstantiateObject(ITransactionHandler transactionHandler, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator);

  /**
   * @return true, if the db is operated in data-mode (the java classes are not required).
   */
  public abstract boolean isDataMode();

}
