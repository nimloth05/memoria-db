package org.memoriadb.test.core.handler.collection;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConcurrentSkipListSetTest extends SetTest {

  @Override
  protected <T> Collection<T> createCollection() {
    return new ConcurrentSkipListSet<T>();
  }

}
