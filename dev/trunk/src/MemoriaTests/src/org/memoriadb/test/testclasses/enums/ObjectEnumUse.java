package org.memoriadb.test.testclasses.enums;

public class ObjectEnumUse {
  
  private Object fEnum;
  
  public TestEnum getEnum() {
    return (TestEnum)fEnum;
  }

  public void setEnum(TestEnum enum1) {
    fEnum = enum1;
  }


}
