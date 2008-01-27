package org.memoriadb.example;

public class Person {

  public static final String NAME_FIELD = "fName";

  private String fName;

  public Person() {}
  
  public Person(String string) {
    fName = string;
  }
  
  public String getName() {
    return fName;
  }
  
}
