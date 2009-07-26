package org.memoriadb.test.handler.testclassses;

import org.memoriadb.test.testclasses.StringObject;

import java.util.ArrayList;
import java.util.List;

public class SimpleTestObjectArrayListContainer implements IArrayListContainer {
  
  private final List<StringObject> fList = new ArrayList<StringObject>();

  public Iterable<StringObject> elements() {
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
      fList.add(new StringObject(Integer.toString(i)));
    }
  }
  
  public StringObject get(int index) {
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
