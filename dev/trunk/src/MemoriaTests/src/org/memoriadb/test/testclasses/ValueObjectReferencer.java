package org.memoriadb.test.testclasses;

import org.memoriadb.ValueObject;

/**
 * This class is a valueObject and references an object.
 * @author sandro
 *
 */
@ValueObject
public class ValueObjectReferencer {
  
  private Object fObject;

  public Object getObject() {
    return fObject;
  }

  public void setObject(Object object) {
    fObject = object;
  }

}
