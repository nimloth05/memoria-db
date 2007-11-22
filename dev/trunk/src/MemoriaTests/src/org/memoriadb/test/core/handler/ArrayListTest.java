package org.memoriadb.test.core.handler;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.handler.testclassses.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

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
    
    IObjectId objId = saveAll(container);
    
    reopen();
    IArrayListContainer container_l1 = fObjectStore.getObject(objId);
    assertEquals(count, container_l1.size());
    assertEquals(container, container_l1);
  }
  
  
  
  
}
