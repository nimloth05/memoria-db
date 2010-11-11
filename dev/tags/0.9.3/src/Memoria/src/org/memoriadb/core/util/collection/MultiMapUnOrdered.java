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

/*
 * Created on 07.12.2006
 */
package org.memoriadb.core.util.collection;

import java.util.*;

/**
 * Implements a hash map that maps keys of type <tt>K</tt> to a set of type <tt>V</tt>.
 * A key cannot map to an empty set, that is, when the last element of a list is removed,
 * the corresponding key is removed as well. The set of values for a key is unordered.
 * 
 * @author Micha
 */
public class MultiMapUnOrdered<K, V> extends AbstractMultiMap<K, V> {
  
  private static final int     DEFAULT_SET_SIZE = 4;
  
  private final Map<K, Set<V>> fMap             = new HashMap<K, Set<V>>();
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    MultiMapUnOrdered<?, ?> other = (MultiMapUnOrdered<?, ?>)obj;
    if (!fMap.equals(other.fMap)) return false;
    return true;
  }
  
  /**
   * @return the unmodifiable collection of values associated with the given key.
   *         If the key is not in the map, an empty collection is returned.
   */
  @Override
  public Set<V> get(K key) {
    if (!contains(key)) return Collections.emptySet();
    return Collections.unmodifiableSet(internalGet(key));
  }

  @Override
  public int hashCode() {
    return fMap.hashCode() + 4710013;
  }
  
  @Override
  public String toString() {
    return fMap.toString();
  }
  
  protected Set<V> createSet() {
    return new HashSet<V>(DEFAULT_SET_SIZE);
  }
  
  @Override
  protected Map<K, ? extends Set<V>> getMap() {
    return fMap;
  }
  
  @Override
  protected Set<V> getOrCreate(K key) {
    Set<V> result = fMap.get(key);
    if (result == null) {
      result = createSet();
      fMap.put(key, result);
    }
    return result;
  }
  
  /**
   * Returns the list of values associated with key.
   * @pre the map contains the key
   */
  @Override
  protected Set<V> internalGet(K key) {
    Set<V> result = getMap().get(key);
    if (result == null) throw new IllegalStateException("key "+key+" not found");
    return result;
  }
  
}
