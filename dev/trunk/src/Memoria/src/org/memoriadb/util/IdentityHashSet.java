package org.memoriadb.util;

import java.util.*;

import org.java.patched.PIdentityHashMap;

public class IdentityHashSet<T> extends AbstractSet<T> {
  
  private static final Object sMarkerValue = new Object();
  private final PIdentityHashMap<T, Object> fBackend = new PIdentityHashMap<T, Object>();

  public IdentityHashSet() {
  } 
  
  public IdentityHashSet(Collection<T> collection) {
    addAll(collection);
  }

  @Override
  public boolean add(T e) {
    return fBackend.put(e, sMarkerValue) == null;
  }

  @Override
  public void clear() {
    fBackend.clear();
  }
  
  @Override
  public boolean contains(Object o) {
    return fBackend.containsKey(o);
  }
  
  @Override
  public boolean containsAll(Collection<?> c) {
    return fBackend.keySet().containsAll(c);
  }

  @Override
  public Iterator<T> iterator() {
    return fBackend.keySet().iterator();
  }
  
  @Override
  public boolean remove(Object obj) {
    return fBackend.remove(obj) == sMarkerValue;
  }
  
  @Override
  public boolean removeAll(Collection<?> c) {
    return fBackend.keySet().removeAll(c);
  }
  
  @Override
  public boolean retainAll(Collection<?> c) {
    return fBackend.keySet().retainAll(c);
  }

  @Override
  public int size() {
    return fBackend.keySet().size();
  }
  
  

}
