package org.memoriadb.test.core.testclasses;

public class ArrayContainer {
  
  public SimpleTestObj[] fArray = new SimpleTestObj[1];
  
  public void set() {
    fArray[0] = new SimpleTestObj("1", 1);
  }

}
