package bootstrap.core.testclasses;

public class WrongHashTestObj {
  
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof WrongHashTestObj)) return false;
    return hashCode() == obj.hashCode();
  }
  
  @Override
  public int hashCode() {
    return System.identityHashCode(this);
  }

}
