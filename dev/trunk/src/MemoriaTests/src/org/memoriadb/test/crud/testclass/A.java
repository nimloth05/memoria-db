package org.memoriadb.test.crud.testclass;

/**
 * Simple Test-class with a reference to B
 * 
 * @author msc
 */
public class A {

  private B fB;

  public A() {}
  
  public A(B b) {
    super();
    fB = b;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final A other = (A) obj;
    if (fB == null) {
      if (other.fB != null) return false;
    }
    else if (!fB.equals(other.fB)) return false;
    return true;
  }

  public B getB() {
    return fB;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fB == null) ? 0 : fB.hashCode());
    return result;
  }

  public void setB(B b) {
    fB = b;
  }

}
