package org.memoriadb.loadtests;

import java.util.*;

import org.memoriadb.core.*;
import org.memoriadb.test.core.AbstractObjectStoreTest;
import org.memoriadb.test.core.testclasses.*;

public class LoadTest extends AbstractObjectStoreTest {
  
  
  @SuppressWarnings("nls")
  public void test_save_objectref() throws Exception {
    List<Object> objects = new ArrayList<Object>();
    
    for(int i = 0; i < 15000; ++i) {
      Referencer composite = new Referencer();
      composite.set(TestObj.class, "1");
      objects.add(composite);
    }
    
    save(objects.toArray());
    fStore.checkSanity();
    
    List<Referencer> allSavedObjects = getAll(Referencer.class);
    for(Referencer ref: allSavedObjects) {
      Object obj = ref.get();
      long objectId = fStore.getObjectId(obj);
      assertSame("id collision: "+objectId, obj, fStore.getObjectById(objectId));
    }
    
    reopen();
    
    Referencer composite = getAll(Referencer.class).get(0);
    assertNotNull("1", composite.getStringValueFromReferencee());
  }
  
}
