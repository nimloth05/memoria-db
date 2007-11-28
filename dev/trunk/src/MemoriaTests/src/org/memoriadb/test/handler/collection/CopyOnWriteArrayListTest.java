package org.memoriadb.test.handler.collection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new CopyOnWriteArrayList<T>();
  }

}
