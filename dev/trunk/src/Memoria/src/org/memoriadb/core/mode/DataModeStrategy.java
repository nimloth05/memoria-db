package org.memoriadb.core.mode;

import org.memoriadb.core.TransactionHandler;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class DataModeStrategy implements IModeStrategy {
 
  @Override
  public IObjectId addMemoriaClassIfNecessary(final TransactionHandler transactionHandler, Object obj) {
    if (!(obj instanceof IDataObject)) throw new MemoriaException("We are in DBMode.data, but the added object is not of type IDataObject");
    
    IDataObject dataObject = (IDataObject) obj;
    if (!transactionHandler.containsId(dataObject.getMemoriaClassId())) throw new MemoriaException("DataObject has no valid memoriaClassId: " + obj);
    
    return dataObject.getMemoriaClassId();
  }

  @Override
  public void checkCanInstantiateObject(TransactionHandler transactionHandler, IObjectId memoriaClassId, IInstantiator instantiator) {
    // FIXME was muss hier überprüft werden?
    // --> Hier müss überprüft werden, ob zum Beispiel die memoriaClassId vorhanden ist oder ähnliches.
  }


  @Override
  public void checkObject(Object obj) {}

  @Override
  public boolean isDataMode() {
    return true;
  }
  
}
