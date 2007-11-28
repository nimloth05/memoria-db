package org.memoriadb.test.testclasses;

public class OuterClass {
  
  private class PrivateInnerClass {
    
    public PrivateInnerClass() {}
    
  }
  
  public PrivateInnerClass getInnerClass() {
    return new PrivateInnerClass();
  }

}
