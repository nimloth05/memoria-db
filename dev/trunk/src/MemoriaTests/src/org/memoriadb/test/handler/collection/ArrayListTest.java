package org.memoriadb.test.handler.collection;

import java.util.*;

public class ArrayListTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new ArrayList<T>();
  }
  
}
