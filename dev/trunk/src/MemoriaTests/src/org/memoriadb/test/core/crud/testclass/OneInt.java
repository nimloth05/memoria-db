package org.memoriadb.test.core.crud.testclass;

/**
 * Test-class with one int attribute.
 * @author msc
 *
 */
public class OneInt {
  private int fInt;
  
  public OneInt() {
  }

  public OneInt(int i) {
    fInt = i;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final OneInt other = (OneInt) obj;
    if (fInt != other.fInt) return false;
    return true;
  }

  public int getInt() {
    return fInt;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fInt;
    return result;
  }

  public void setInt(int i) {
    fInt = i;
  }
  
  @Override
  public String toString() {
    return "OneInt " +fInt;
  }
  
  
  
}
