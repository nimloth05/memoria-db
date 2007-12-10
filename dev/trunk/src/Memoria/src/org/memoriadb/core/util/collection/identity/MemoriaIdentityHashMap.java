package org.memoriadb.core.util.collection.identity;

import java.util.*;

public class MemoriaIdentityHashMap<K, V> implements Map<K, V> {
  
  
  private final IdentityHashMap<K, V> fMap = new IdentityHashMap<K, V>();

  @Override
  public void clear() {
    fMap.clear();
  }

  @Override
  public boolean containsKey(Object key) {
    return fMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return fMap.containsValue(value);
  }

  @Override
  public Set<java.util.Map.Entry<K, V>> entrySet() {
    return fMap.entrySet();
  }

  @Override
  public V get(Object key) {
    return fMap.get(key);
  }

  @Override
  public boolean isEmpty() {
    return fMap.isEmpty();
  }

  @Override
  public Set<K> keySet() {
    return new MemoriaIdentityHashMapKeySet<K>(fMap.keySet());
  }

  @Override
  public V put(K key, V value) {
    return fMap.put(key, value);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    fMap.putAll(m);
  }

  @Override
  public V remove(Object key) {
    return fMap.remove(key);
  }

  @Override
  public int size() {
    return fMap.size();
  }

  @Override
  public Collection<V> values() {
    return fMap.values();
  }

}
