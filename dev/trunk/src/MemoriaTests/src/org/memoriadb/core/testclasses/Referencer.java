package org.memoriadb.core.testclasses;


public class Referencer {
  
  private TestObj fObject;
  
  public void set(String text) {
    fObject = new TestObj(text, 0);
  }
  
  public TestObj get() {
    return fObject;
  }
  
}
