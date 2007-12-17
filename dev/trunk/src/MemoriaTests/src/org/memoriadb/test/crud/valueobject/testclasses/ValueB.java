package org.memoriadb.test.crud.valueobject.testclasses;

import org.memoriadb.ValueObject;

@ValueObject
public class ValueB extends ValueA {

  public String fValueB;
  
  public ValueB() {
  }

  public ValueB(String dataA, String valueB) {
    super(dataA);
    fValueB = valueB;
  }
  
}
