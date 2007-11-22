package org.memoriadb.core.mode;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.exception.MemoriaException;

public class DataModeStrategy implements IModeStrategy {
 
  @Override
  public IObjectId addMemoriaClassIfNecessary(final TrxHandler trxHandler, Object obj) {
    if (!(obj instanceof IDataObject)) throw new MemoriaException("We are in DBMode.data, but the added object is not of type IDataObject");
    
    IDataObject dataObject = (IDataObject) obj;
    if (!trxHandler.containsId(dataObject.getMemoriaClassId())) throw new MemoriaException("DataObject has no valid memoriaClassId");
    
    return dataObject.getMemoriaClassId();
  }

  @Override
  public void checkCanInstantiateObject(ITrxHandler trxHandler, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator) {}


  @Override
  public boolean isInDataMode() {
    return true;
  }
  
}