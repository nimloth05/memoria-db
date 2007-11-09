package org.memoriadb.core.handler;

import org.memoriadb.core.id.IObjectId;

/**
 * This Interface will returned if you perform a query in DBMode.data.
 * @author sandro
 *
 */
public interface IDataObject {
  
  /**
   * 
   * @return the objectId for the memoriaClass for this Object.
   */
  public IObjectId getMemoriaClassId();

}
