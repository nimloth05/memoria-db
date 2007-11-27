package org.memoriadb.handler;

import org.memoriadb.id.IObjectId;

/**
 * This Interface will returned if you perform a query in DBMode.data.
 * 
 * When adding data in DBMode.data, use also IDataObject. The handler to serialize the object
 * is chosen by means of the MemoriaClass and must be able to handle the given subclass of IDataObject.
 * 
 * @author sandro
 *
 */
public interface IDataObject {
  
  /**
   * 
   * @return ObjectId of the memoriaClass for this Object.
   */
  public IObjectId getMemoriaClassId();

}
