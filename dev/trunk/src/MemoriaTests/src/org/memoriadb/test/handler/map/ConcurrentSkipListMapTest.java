package org.memoriadb.test.handler.map;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListMapTest extends AbstractMapTest {

  @Override
  public void test_mixed_map() {
  // needs comparable keys
  }

  @Override
  public void test_null_value() {
  // not supported für ConcurrentMap
  }

  @Override
  protected <K, V> Map<K, V> createMap() {
    return new ConcurrentSkipListMap<K, V>();
  }

}
