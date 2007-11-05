package org.memoriadb.test.core.collection;

import org.memoriadb.test.core.AbstractObjectStoreTest;
import org.memoriadb.test.core.collection.testclassses.*;

public class ArrayListTest extends AbstractObjectStoreTest {

  //FIXME Sollten wir hier mit wrongHashCode testen? so
  public void test_object_arrayList() {
    test(new SimpleTestObjectArrayListContainer());
  }
  
  public void test_primitive_arrayList() {
    test(new IntegerArrayListContainer());
  }
  
  private void test(IArrayListContainer container) {
    final int count = 2;
    
    container.fill(count);
    
    long objId = saveAll(container);
    
    reopen();
    IArrayListContainer container_l1 = fStore.getObject(objId);
    assertEquals(count, container_l1.size());
    assertEquals(container, container_l1);
  }
  
  
  
  
}