package org.memoriadb.test.handler.map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.memoriadb.testutil.AbstractMemoriaTest;

public class QueryTest extends AbstractMemoriaTest {
  public void test() {
    save(new HashMap<Object,Object>());
    save(new ConcurrentHashMap<Object,Object>());
    
    assertEquals(2, fObjectStore.query(AbstractMap.class).size());
  }
}
