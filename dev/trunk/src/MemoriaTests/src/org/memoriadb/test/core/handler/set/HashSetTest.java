package org.memoriadb.test.core.handler.set;

import java.util.*;

public class HashSetTest extends AbstractSetTest {

  @Override
  protected <T> Set<T> createSet() {
    return new HashSet<T>();
  }

}
