package org.memoriadb.core.testclasses;

public class ArrayContainer {
  
  public SimpleTestObj[] fArray = new SimpleTestObj[1];
  
  public void set() {
    fArray[0] = new SimpleTestObj();
  }

}
