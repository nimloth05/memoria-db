package org.memoriadb.core.testclasses;

public class SimpleTestObj {
  
  private String fString;
  private int fInt;
  
  public SimpleTestObj() {}
  
  public SimpleTestObj(String string) {
    this(string, 1);
  }
  
  public SimpleTestObj(String string, int i) {
    fString = string;
    fInt = i;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final SimpleTestObj other = (SimpleTestObj) obj;
    if (fInt != other.fInt) return false;
    if (fString == null) {
      if (other.fString != null) return false;
    }
    else if (!fString.equals(other.fString)) return false;
    return true;
  }

  public String getString() {
    return fString;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fInt;
    result = prime * result + ((fString == null) ? 0 : fString.hashCode());
    return result;
  }
  
  @Override
  public String toString() {
    return "s: "+fString + "i: " + fInt;
  }
}
