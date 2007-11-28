package org.memoriadb.test.handler.map;

import java.util.*;

public class LinkedHashMapTest extends AbstractMapTest {

  @Override
  protected <K, V> Map<K, V> createMap() {
    return new LinkedHashMap<K,V>();
  }

}
