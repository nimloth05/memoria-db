package org.memoriadb.test.core.crud.testclass;

public class TransientClass {
  public transient int fInt;
  public transient A fA;
  
  public TransientClass() {
    
  }
  
  public TransientClass(int i, A a) {
    fInt = i;
    fA = a;
  }
}
