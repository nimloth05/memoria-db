package org.memoriadb.loadtests;

import java.util.*;

import org.memoriadb.test.core.testclasses.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class LoadTest extends AbstractObjectStoreTest {
  
  
  @SuppressWarnings("nls")
  public void test_save_objectref() throws Exception {
    List<Object> objects = new ArrayList<Object>();
    
    for(int i = 0; i < 150; ++i) {
      Referencer composite = new Referencer();
      composite.set(SimpleTestObj.class, "1");
      objects.add(composite);
    }
    
    saveAll(objects.toArray());
    fStore.checkSanity();
    
    List<Referencer> allSavedObjects = getAll(Referencer.class);
    for(Referencer ref: allSavedObjects) {
      Object obj = ref.get();
      long objectId = fStore.getObjectId(obj);
      assertSame("id collision: "+objectId, obj, fStore.getObject(objectId));
    }
    
    reopen();
    
    Referencer composite = getAll(Referencer.class).get(0);
    assertNotNull("1", composite.getStringValueFromReferencee());
  }
  
}
