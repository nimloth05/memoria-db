package org.memoriadb.test.handler.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest extends AbstractMapTest {

  @Override
  public void test_null_value() {
    // not supported für ConcurrentMap
  }
  
  @Override
  protected <K, V> Map<K, V> createMap() {
    return new ConcurrentHashMap<K,V>();
  }

}
