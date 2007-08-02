package org.memoriadb.core.testclasses;

public class WrongHashCodeIdentityHashCode {
  
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof WrongHashCodeIdentityHashCode)) return false;
    return hashCode() == obj.hashCode();
  }
  
  @Override
  public int hashCode() {
    return System.identityHashCode(this);
  }

}
