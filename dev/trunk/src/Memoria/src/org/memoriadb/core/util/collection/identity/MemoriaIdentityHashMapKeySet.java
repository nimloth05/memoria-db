package org.memoriadb.core.util.collection.identity;

import java.util.*;

public class MemoriaIdentityHashMapKeySet<K> implements Set<K> {
  
  private final Set<K> fKeySet;
  
  public MemoriaIdentityHashMapKeySet(Set<K> set) {
    fKeySet = set;
  }

  public boolean add(K e) {
    return fKeySet.add(e);
  }

  public boolean addAll(Collection<? extends K> c) {
    throw new UnsupportedOperationException();
  }

  public void clear() {
    fKeySet.clear();
  }

  public boolean contains(Object o) {
    return fKeySet.contains(o);
  }

  public boolean containsAll(Collection<?> c) {
    return fKeySet.containsAll(c);
  }

  @Override
  public boolean equals(Object o) {
    return fKeySet.equals(o);
  }

  @Override
  public int hashCode() {
    return fKeySet.hashCode();
  }

  public boolean isEmpty() {
    return fKeySet.isEmpty();
  }

  public Iterator<K> iterator() {
    return fKeySet.iterator();
  }

  public boolean remove(Object o) {
    return fKeySet.remove(o);
  }

  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException("Bug in IdentityHashMap");
  }

  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException("Bug in IdentityHashMap");
  }

  public int size() {
    return fKeySet.size();
  }

  public Object[] toArray() {
    return fKeySet.toArray();
  }

  public <T> T[] toArray(T[] a) {
    return fKeySet.toArray(a);
  }
  
}
