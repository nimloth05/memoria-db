package org.memoriadb.test.handler.testclassses;

import org.memoriadb.test.testclasses.SimpleTestObj;

public class HashSetKey {
  
  private SimpleTestObj fTestObj;

  public HashSetKey() {
    
  }
  
  public HashSetKey(String str) {
    fTestObj = new SimpleTestObj(str);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final HashSetKey other = (HashSetKey) obj;
    if (fTestObj == null) {
      if (other.fTestObj != null) return false;
    }
    else if (!fTestObj.equals(other.fTestObj)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fTestObj == null) ? 0 : fTestObj.hashCode());
    return result;
  }
  
  
}