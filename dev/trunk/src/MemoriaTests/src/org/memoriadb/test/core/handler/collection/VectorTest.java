package org.memoriadb.test.core.handler.collection;

import java.util.*;

public class VectorTest extends ListTest{

  @Override
  protected <T> List<T> createCollection() {
    return new Vector<T>();
  }

}
