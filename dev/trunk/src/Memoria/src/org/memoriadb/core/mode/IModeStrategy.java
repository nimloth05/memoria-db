package org.memoriadb.core.mode;

import org.memoriadb.core.TransactionHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

/**
 * @author msc
 * 
 */
public interface IModeStrategy {
  /**
   * @return ObjectId of the MemoriaClass for the given obj
   */
  public IObjectId addMemoriaClassIfNecessary(TransactionHandler transactionHandler, Object obj);

  /**
   * Before an object is added to the ObjectRepository, it is checked for instantiability.
   */
  public void checkCanInstantiateObject(TransactionHandler transactionHandler, IObjectId memoriaClassId, IInstantiator instantiator);

  public void checkObject(Object obj);

  /**
   * @return true, if the db is operated in data-mode (the java classes are not required).
   */
  public boolean isDataMode();

}
