package org.memoriadb.core.testclasses.serialization;

import java.io.Serializable;

public class Serialization implements Serializable {
  
  public String fString;
  public int fInt;
  
  public Serialization() {}
  
  public Serialization(String string) {
    fString = string;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final Serialization other = (Serialization) obj;
    if (fInt != other.fInt) return false;
    if (fString == null) {
      if (other.fString != null) return false;
    }
    else if (!fString.equals(other.fString)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fInt;
    result = prime * result + ((fString == null) ? 0 : fString.hashCode());
    return result;
  }
  
  

}
