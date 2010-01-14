package org.memoriadb.test.refactoring.old;

public class Address {
  
  String fAddress;
  
  public Address() {
    
  }
  
  public String getAddress() {
    return fAddress;
  }
  
  public void setAddress(String address) {
    fAddress = address;
  }
  
  @Override
  public String toString() {
    return getAddress();
  }
}
