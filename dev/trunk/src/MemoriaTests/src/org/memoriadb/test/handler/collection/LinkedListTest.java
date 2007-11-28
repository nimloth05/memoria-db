package org.memoriadb.test.handler.collection;

import java.util.*;

public class LinkedListTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new LinkedList<T>();
  }

}
