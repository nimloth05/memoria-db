package org.memoriadb.test.handler.collection;

import java.util.*;

public class VectorTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new Vector<T>();
  }

}
