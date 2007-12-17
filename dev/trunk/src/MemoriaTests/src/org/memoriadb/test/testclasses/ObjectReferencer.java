package org.memoriadb.test.testclasses;

public class ObjectReferencer {
  
  private Object fObject;

  public ObjectReferencer() {
    
  }
  
  public ObjectReferencer(Object object) {
    fObject = object;
  }

  public Object getObejct() {
    return fObject;
  }
  
  public void setObject(Object object) {
    fObject = object;
  }

}
