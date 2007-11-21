package org.memoriadb.test.core.handler.collection;

import java.util.*;

public class StackTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new Stack<T>();
  }

}
