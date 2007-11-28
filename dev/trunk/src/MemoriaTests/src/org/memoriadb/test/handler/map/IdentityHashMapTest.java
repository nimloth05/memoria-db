package org.memoriadb.test.handler.map;

import java.util.*;

public class IdentityHashMapTest extends AbstractMapTest {

  @Override
  public void test_map_containing_map_with_back_ref() {
  }

  @Override
  protected <K, V> Map<K, V> createMap() {
    return new IdentityHashMap<K,V>();
  }
}
