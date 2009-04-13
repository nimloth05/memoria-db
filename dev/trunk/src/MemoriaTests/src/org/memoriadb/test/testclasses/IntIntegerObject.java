package org.memoriadb.test.testclasses;

public class IntIntegerObject {

  private int fInt;
  private Integer fInteger;

  public IntIntegerObject() {}

  public IntIntegerObject(int i, Integer integer) {
    super();
    fInt = i;
    fInteger = integer;
  }

  public int getInt() {
    return fInt;
  }

  public Integer getInteger() {
    return fInteger;
  }

  public void setInt(int i) {
    fInt = i;
  }
  
  public void setInteger(Integer integer) {
    fInteger = integer;
  }

}
