package org.memoriadb.test.refactoring.nu;

public class Address {
  
  String fStrret;
  String fCity;
   
  public Address() {
    
  }
  
  public String getAddress() {
    return fStrret;
  }
  
  public String getCity() {
    return fCity;
  }

  public void setCity(String city) {
    fCity = city;
  }

  public void setStreet(String street) {
    fStrret = street;
  } 
  
}
