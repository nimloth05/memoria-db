package org.memoriadb.test.testclasses.inheritance;

import org.memoriadb.test.testclasses.SimpleTestObj;

public class C extends B {
  
  public short fShort;

  
  public C() {
    
  }
  
  public C(int i, Long l, String string, boolean b, SimpleTestObj testObj, short s) {
    super(i, l, string, b, testObj);
    fShort = s;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    final C other = (C) obj;
    if (fShort != other.fShort) return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + fShort;
    return result;
  }

  
}
