package org.memoriadb.test.core.handler.list;

import java.util.*;

public class ArrayListTest extends ListTest{

  @Override
  protected <T> List<T> createList() {
    return new ArrayList<T>();
  }
  
}
