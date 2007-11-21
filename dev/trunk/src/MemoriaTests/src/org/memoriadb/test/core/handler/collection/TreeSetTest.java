package org.memoriadb.test.core.handler.collection;

import java.util.*;

public class TreeSetTest extends SetTest {

  @Override
  protected <T> Collection<T> createCollection() {
    return new TreeSet<T>();
  }

}
