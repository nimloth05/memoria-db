package org.memoriadb.test.testclasses;

public class IntObject {
  
  private int fInt = 0;

  public IntObject() {}
  
  public IntObject(int i) {
    fInt = i;
  }

  public int getInt() {
    return fInt;
  }

  public void setInt(int i) {
    fInt = i;
  }

  @Override
  public String toString() {
    return "IntObject{" + "fInt=" + fInt + '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final IntObject intObject = (IntObject) o;

    if (fInt != intObject.fInt) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return fInt;
  }
}
