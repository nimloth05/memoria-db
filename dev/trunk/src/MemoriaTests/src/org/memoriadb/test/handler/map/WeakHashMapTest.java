package org.memoriadb.test.handler.map;

import java.util.*;

public class WeakHashMapTest extends AbstractMapTest {

  @Override
  protected <K, V> Map<K, V> createMap() {
    return new WeakHashMap<K,V>();
  }

}
