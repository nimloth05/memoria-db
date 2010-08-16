/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.core.util.collection.identity;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MemoriaIdentityHashMapKeySet<K> implements Set<K> {
  
  private final Set<K> fKeySet;
  
  public MemoriaIdentityHashMapKeySet(Set<K> set) {
    fKeySet = set;
  }

  @Override
  public boolean add(K e) {
    return fKeySet.add(e);
  }

  @Override
  public boolean addAll(Collection<? extends K> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    fKeySet.clear();
  }

  @Override
  public boolean contains(Object o) {
    return fKeySet.contains(o);
  }

  @Override
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

  @Override
  public boolean isEmpty() {
    return fKeySet.isEmpty();
  }

  @Override
  public Iterator<K> iterator() {
    return fKeySet.iterator();
  }

  @Override
  public boolean remove(Object o) {
    return fKeySet.remove(o);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException("Bug in IdentityHashMap");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException("Bug in IdentityHashMap");
  }

  @Override
  public int size() {
    return fKeySet.size();
  }

  @Override
  public Object[] toArray() {
    return fKeySet.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return fKeySet.toArray(a);
  }
  
  @Override
  public String toString() {
    return fKeySet.toString();
  }
}
