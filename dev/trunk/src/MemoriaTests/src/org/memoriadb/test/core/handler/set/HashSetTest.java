package org.memoriadb.test.core.handler.set;

import java.util.*;


public class HashSetTest extends SetTest {

  @Override
  protected <T> Collection<T> createCollection() {
    return new HashSet<T>();
  }

  

}
