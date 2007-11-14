package org.memoriadb.test.core.handler.list;

import java.util.*;

public class StackTest extends ListTest{

  @Override
  protected <T> List<T> createList() {
    return new Stack<T>();
  }

}
