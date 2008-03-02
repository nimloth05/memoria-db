package org.memoriadb.test.testclasses;

public class ObjectReferencer {
  
  /**
   * This string  muss be in sync with the fieldname.
   */
  public static final String FIELD_NAME = "fObject";
  
  private Object fObject;

  public ObjectReferencer() {
    
  }
  
  public ObjectReferencer(Object object) {
    fObject = object;
  }

  public Object getObject() {
    return fObject;
  }
  
  public void setObject(Object object) {
    fObject = object;
  }

}
