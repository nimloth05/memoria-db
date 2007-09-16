package org.memoriadb.test.core.collection.testclassses;

import java.util.*;

import org.memoriadb.test.core.testclasses.SimpleTestObj;

public class SimpleTestObjectArrayListContainer implements IArrayListContainer {
  
  private final List<SimpleTestObj> fList = new ArrayList<SimpleTestObj>();

  public Iterable<SimpleTestObj> elements() {
    return fList;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final SimpleTestObjectArrayListContainer other = (SimpleTestObjectArrayListContainer) obj;
    if (fList == null) {
      if (other.fList != null) return false;
    }
    else if (!fList.equals(other.fList)) return false;
    return true;
  }
  
  public void fill(int count) {
    for(int i = 0; i < count; ++i) {
      fList.add(new SimpleTestObj(Integer.toString(i)));
    }
  }
  
  public SimpleTestObj get(int index) {
    return fList.get(index);
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
