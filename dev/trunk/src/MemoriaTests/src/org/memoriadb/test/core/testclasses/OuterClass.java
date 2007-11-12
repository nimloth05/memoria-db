package org.memoriadb.test.core.testclasses;

public class OuterClass {
  
  private class PrivateInnerClass {
    
    public PrivateInnerClass() {}
    
  }
  
  public PrivateInnerClass getInnerClass() {
    return new PrivateInnerClass();
  }

}
