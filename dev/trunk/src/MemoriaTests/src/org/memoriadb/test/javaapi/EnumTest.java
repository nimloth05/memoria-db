package org.memoriadb.test.javaapi;

import junit.framework.TestCase;

public class EnumTest extends TestCase {
  
  
  private enum TestEnum {
    a,
    b {
      
    };
  }
  
  
  public void test_class_is_enum() {
    assertTrue(TestEnum.a.getClass().isEnum());
    assertFalse(TestEnum.b.getClass().isEnum());
    
    assertEquals(TestEnum.values().length, TestEnum.a.getClass().getEnumConstants().length);
    assertNull(TestEnum.b.getClass().getEnumConstants());
  }

}
