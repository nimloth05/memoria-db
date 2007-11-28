package org.memoriadb.test.handler.collection;

import java.util.*;


public class HashSetTest extends SetTest {

  @Override
  protected <T> Collection<T> createCollection() {
    return new HashSet<T>();
  }

  

}
