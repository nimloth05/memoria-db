package org.memoriadb.test.core.handler.collection;

import java.util.*;

public class ArrayListTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new ArrayList<T>();
  }
  
}
