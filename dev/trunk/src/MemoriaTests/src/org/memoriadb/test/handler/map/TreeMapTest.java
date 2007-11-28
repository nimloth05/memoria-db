package org.memoriadb.test.handler.map;

import java.util.*;

public class TreeMapTest extends AbstractMapTest {

  @Override
  public void test_mixed_map() {
  // needs comparable keys
  }
  
  @Override
  protected <K, V> Map<K, V> createMap() {
    return new TreeMap<K,V>();
  }

}
