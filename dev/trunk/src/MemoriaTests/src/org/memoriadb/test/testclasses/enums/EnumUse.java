package org.memoriadb.test.testclasses.enums;

public class EnumUse {
  
  private TestEnum fEnum;
  
  public EnumUse() {}
  
  public EnumUse(TestEnum enum1) {
    fEnum = enum1;
  }

  public TestEnum getEnum() {
    return fEnum;
  }

  public void setEnum(TestEnum enum1) {
    fEnum = enum1;
  }

}
