package org.memoriadb.test.handler.collection;

import java.util.*;

public class StackTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new Stack<T>();
  }

}
