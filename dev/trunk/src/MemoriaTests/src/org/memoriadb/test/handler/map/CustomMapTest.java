package org.memoriadb.test.handler.map;

import java.util.HashMap;

import org.memoriadb.testutil.AbstractMemoriaTest;

/**
 * Tests the behaviour when a class dervied from HashMap ist stored.
 *
 * @author msc
 *
 */
public class CustomMapTest extends AbstractMemoriaTest {

  static class CustomMap<K,V> extends HashMap<K,V> {
  }

  public void test() {
    
    CustomMap<String, String> map = new CustomMap<String, String>();
    
    save(map);
    
  }
}
