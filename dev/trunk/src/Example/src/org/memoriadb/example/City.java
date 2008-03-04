package org.memoriadb.example;

public class City {
  
  private String fName;
  
  public City(String string) {
    fName = string;
  }

  //For persistence
  City() {}

  public String getName() {
    return fName;
  }

  public void setName(String name) {
    fName = name;
  }
}
