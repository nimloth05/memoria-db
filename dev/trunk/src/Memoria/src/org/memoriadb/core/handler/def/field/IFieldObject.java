package org.memoriadb.core.handler.def.field;


public interface IFieldObject {
  
  public Object get(String fieldName);
  
  public Object getObject();
  
  public void set(String fieldName, Object value);

}
