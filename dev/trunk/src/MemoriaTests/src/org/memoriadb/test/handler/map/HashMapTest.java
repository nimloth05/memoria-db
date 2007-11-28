package org.memoriadb.test.handler.map;

import java.util.*;

public class HashMapTest extends AbstractMapTest {

  @Override
  protected <K, V> Map<K, V> createMap() {
    return new HashMap<K, V>();
  }
  
}
