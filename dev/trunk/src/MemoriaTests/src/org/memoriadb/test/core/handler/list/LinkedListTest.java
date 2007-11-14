package org.memoriadb.test.core.handler.list;

import java.util.*;

public class LinkedListTest extends ListTest{

  @Override
  protected <T> List<T> createList() {
    return new LinkedList<T>();
  }

}
