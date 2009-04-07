package org.memoriadb.test.crud.valueobject.testclasses;

import org.memoriadb.test.testclasses.SimpleTestObj;


public class AnnotatedObjectReferencer extends SimpleTestObj {
  
  private Object fObject;

  public AnnotatedObjectReferencer() {
    
  }
  
  public AnnotatedObjectReferencer(Object object) {
    super(object.toString());
    fObject = object;
  }

  public Object getObject() {
    return fObject;
  }

  public void setObject(Object object) {
    fObject = object;
  }
  
}
