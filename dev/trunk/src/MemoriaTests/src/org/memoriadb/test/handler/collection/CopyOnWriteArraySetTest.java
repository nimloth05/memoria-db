package org.memoriadb.test.handler.collection;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

public class CopyOnWriteArraySetTest extends SetTest {

  @Override
  protected <T> Collection<T> createCollection() {
    return new CopyOnWriteArraySet<T>();
  }

}
