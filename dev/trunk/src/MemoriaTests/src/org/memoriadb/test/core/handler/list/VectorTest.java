package org.memoriadb.test.core.handler.list;

import java.util.*;

public class VectorTest extends ListTest{

  @Override
  protected <T> List<T> createList() {
    return new Vector<T>();
  }

}
