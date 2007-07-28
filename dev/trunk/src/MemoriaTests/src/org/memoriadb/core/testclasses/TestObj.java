package org.memoriadb.core.testclasses;

public class TestObj {

  private String fString1;
  private String fString2;
  private String fString3;
  
  private int fI1;
  private int fI2;
  private int fI3;

  public TestObj() {}
  
  public TestObj(String string, int i) {
    fString1 = string;
    fI1 = i;
    
    fString2 = fString1;
    fString3 = fString1;
    
    fI2 = fI1;
    fI3 = fI1;
  }

  public String getString() {
    return fString1;
  }

  public void setString(String string) {
    fString1 = string;
  }

  public int getI() {
    return fI1;
  }

  public void setI(int i) {
    fI1 = i;
  }
}
