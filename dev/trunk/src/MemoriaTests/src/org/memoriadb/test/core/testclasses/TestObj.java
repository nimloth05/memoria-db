package org.memoriadb.test.core.testclasses;

public class TestObj {

  private String fString1;
  
  private int fI1;

  public TestObj() {}
  
  /**
   * This constructor is used by the generic object ref test.
   * @param string
   */
  public TestObj(String string) {
    this(string, 1);
  }
  
  public TestObj(String string, int i) {
    fString1 = string;
    fI1 = i;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final TestObj other = (TestObj) obj;
    if (fI1 != other.fI1) return false;
    else if (!fString1.equals(other.fString1)) return false;
    return true;
  }

  public int getI() {
    return fI1;
  }
  
  public String getString() {
    return fString1;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fI1;
    result = prime * result + ((fString1 == null) ? 0 : fString1.hashCode());
    return result;
  }

  public void setI(int i) {
    fI1 = i;
  }

  public void setString(String string) {
    fString1 = string;
  }

  @Override
  public String toString() {
    return getString();
  }
  
  
  
  
}
