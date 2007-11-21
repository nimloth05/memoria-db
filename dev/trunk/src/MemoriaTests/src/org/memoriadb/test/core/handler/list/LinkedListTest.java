package org.memoriadb.test.core.handler.list;

import java.util.*;

public class LinkedListTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new LinkedList<T>();
  }

}
