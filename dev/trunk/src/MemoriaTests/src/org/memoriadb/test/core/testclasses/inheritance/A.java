package org.memoriadb.test.core.testclasses.inheritance;

public class A {

  private int fInt;
  private Long fLong;
  

  public A() {
    
  }
  
  public A(int i, Long l) {
    fInt = i;
    fLong = l;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final A other = (A) obj;
    if (fInt != other.fInt) return false;
    if (fLong == null) {
      if (other.fLong != null) return false;
    }
    else if (!fLong.equals(other.fLong)) return false;
    return true;
  }

  public int getInt() {
    return fInt;
  }

  public Long getLong() {
    return fLong;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fInt;
    result = prime * result + ((fLong == null) ? 0 : fLong.hashCode());
    return result;
  }

  public void setInt(int i) {
    fInt = i;
  }

  public void setLong(Long l) {
    fLong = l;
  }
  
  

}
