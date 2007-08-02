package org.memoriadb.core.testclasses;

public class WrongHashCode {
  
  public String fValue;
  
  public WrongHashCode() {}
  
  public WrongHashCode(String value) {
    fValue = value;
  }
  
  @Override
  public boolean equals(Object object) {
    return true;
  }
  
  public String getString() {
    return fValue;
  }
  
  @Override
  public int hashCode() {
    return 0;
  }
  

}
