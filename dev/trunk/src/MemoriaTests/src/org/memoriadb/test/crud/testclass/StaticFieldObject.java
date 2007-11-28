package org.memoriadb.test.crud.testclass;

public class StaticFieldObject {
  
  public static String sString = "a";
  public static final String sFinalString = "b";
  
  private String fString;
  
  public String getString() {
    return fString;
  }
  
  public void setString(String string) {
    fString = string;
  }

}
