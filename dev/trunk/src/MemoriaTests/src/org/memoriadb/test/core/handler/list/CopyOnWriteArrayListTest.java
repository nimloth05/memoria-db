package org.memoriadb.test.core.handler.list;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new CopyOnWriteArrayList<T>();
  }

}
