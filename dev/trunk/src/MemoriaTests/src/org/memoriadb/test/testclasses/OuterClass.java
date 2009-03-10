package org.memoriadb.test.testclasses;

public class OuterClass {

  private class PrivateInnerClass {
    public void f() {
    }
  }

  public Object getAnonymousInnerclass() {
    return new Object() {
      @SuppressWarnings("unused")
      public void g() {
        
      }
    };
  }
  
  public PrivateInnerClass getPrivateInnerClass() {
    return new PrivateInnerClass();
  }

}
