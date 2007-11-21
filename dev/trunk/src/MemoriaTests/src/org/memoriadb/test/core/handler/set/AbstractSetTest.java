package org.memoriadb.test.core.handler.set;

import java.util.Set;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public abstract class AbstractSetTest extends AbstractObjectStoreTest {
  
  
  public void test_int_test() {
    Set<Integer> intSet = createIntSet();
    reopen(intSet);
  }
  
  protected abstract <T> Set<T> createSet();

  private Set<Integer> createIntSet() {
    Set<Integer> set = createSet();
    set.add(1);
    set.add(2);
    set.add(3);
    return set;
  }

  private void reopen(Set<?> set) {
    IObjectId id = saveAll(set);
    reopen();
    assertEquals(set, get(id));
  }

}
