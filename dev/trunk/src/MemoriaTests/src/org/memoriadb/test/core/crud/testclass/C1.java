package org.memoriadb.test.core.crud.testclass;

/**
 * Cyclic dependancy with C2
 * 
 * @author msc
 * 
 */
public class C1 {
  
  private C2 fC2;
  private String fName;

  public C1() {}
  
  public C1(String name) {
    super();
    fName = name;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final C1 other = (C1) obj;
    if (fName == null) {
      if (other.fName != null) return false;
    }
    else if (!fName.equals(other.fName)) return false;
    return true;
  }

  public C2 getC2() {
    return fC2;
  }

  public String getName() {
    return fName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fName == null) ? 0 : fName.hashCode());
    return result;
  }

  public void setC2(C2 c2) {
    this.fC2 = c2;
  }
  
  

}
