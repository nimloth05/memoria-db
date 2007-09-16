package org.memoriadb.test.core.collection.testclassses;

import java.util.*;

public class IntegerArrayListContainer implements IArrayListContainer {
  
  private final List<Integer> fList = new ArrayList<Integer>();
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final IntegerArrayListContainer other = (IntegerArrayListContainer) obj;
    if (fList == null) {
      if (other.fList != null) return false;
    }
    else if (!fList.equals(other.fList)) return false;
    return true;
  }
  
  public void fill(int count) {
    for(int i = 0; i < count; ++i) {
      fList.add(i);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fList == null) ? 0 : fList.hashCode());
    return result;
  }

  public int size() {
    return fList.size();
  }

}
