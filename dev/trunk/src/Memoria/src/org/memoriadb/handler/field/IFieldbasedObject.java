package org.memoriadb.handler.field;

import org.memoriadb.handler.IDataObject;


public interface IFieldbasedObject extends IDataObject {

  public boolean equalsLangValueObject(String fieldName, Object value);

  public Object get(String fieldName);

  public Object getObject();
  
  public void set(String fieldName, Object value);

}
