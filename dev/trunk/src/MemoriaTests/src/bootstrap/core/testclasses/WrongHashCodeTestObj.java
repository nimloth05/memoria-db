package bootstrap.core.testclasses;

public class WrongHashCodeTestObj {
  
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof WrongHashCodeTestObj)) return false;
    return hashCode() == obj.hashCode();
  }
  
  @Override
  public int hashCode() {
    return System.identityHashCode(this);
  }

}
