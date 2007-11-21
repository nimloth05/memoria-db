package org.memoriadb.test.core.handler.collection;

import java.util.*;

public class LinkedListTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new LinkedList<T>();
  }

}
