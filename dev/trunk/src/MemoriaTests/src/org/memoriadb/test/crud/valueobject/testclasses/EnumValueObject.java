package org.memoriadb.test.crud.valueobject.testclasses;

import org.memoriadb.ValueObject;
import org.memoriadb.test.testclasses.enums.TestEnum;

/**
 * @author sandro
 *
 */
@ValueObject
public class EnumValueObject {
  
  private TestEnum fEnum = TestEnum.a;

  public TestEnum getEnum() {
    return fEnum;
  }

  public void setEnum(TestEnum enum1) {
    fEnum = enum1;
  }
  
}
