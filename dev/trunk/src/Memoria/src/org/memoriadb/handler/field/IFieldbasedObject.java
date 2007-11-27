package org.memoriadb.handler.field;

import org.memoriadb.handler.IDataObject;


public interface IFieldbasedObject extends IDataObject {
  
  public Object get(String fieldName);
  
  public Object getObject();
  
  public void set(String fieldName, Object value);

}
