package org.memoriadb.test.core.handler.list;

import java.util.*;

public class LinkedListTest extends ListTest{

  @Override
  protected List createList() {
    return new LinkedList<Object>();
  }

}
