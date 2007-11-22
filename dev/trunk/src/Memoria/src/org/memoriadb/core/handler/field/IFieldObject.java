package org.memoriadb.core.handler.field;

import org.memoriadb.core.handler.IDataObject;


public interface IFieldObject extends IDataObject {
  
  public Object get(String fieldName);
  
  public Object getObject();
  
  public void set(String fieldName, Object value);

}
