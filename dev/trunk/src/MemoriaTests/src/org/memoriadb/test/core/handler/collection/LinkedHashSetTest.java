package org.memoriadb.test.core.handler.collection;

import java.util.*;

public class LinkedHashSetTest extends SetTest {

  @Override
  protected <T> Collection<T> createCollection() {
    return new LinkedHashSet<T>();
  }

}
