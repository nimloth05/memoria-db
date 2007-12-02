package org.memoriadb.loadtests;

import java.util.*;

import org.memoriadb.TestMode;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.*;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class LoadTest extends AbstractMemoriaTest {
  
  
  @SuppressWarnings("nls")
  public void test_save_objectref() throws Exception {
    List<Object> objects = new ArrayList<Object>();
    
    beginUpdate();
    
    for(int i = 0; i < 30000; ++i) {
      Referencer composite = new Referencer();
      composite.set(SimpleTestObj.class, "1");
      objects.add(composite);
      saveAll(composite);
    }
    
    endUpdate();
    
    List<Referencer> allSavedObjects = query(Referencer.class);
    for(Referencer ref: allSavedObjects) {
      Object obj = ref.get();
      IObjectId objectId = fObjectStore.getId(obj);
      assertSame("id collision: "+objectId, obj, fObjectStore.get(objectId));
    }
    
    reopen();
    
    Referencer composite = query(Referencer.class).get(0);
    assertNotNull("1", composite.getStringValueFromReferencee());
  }
  
  public void test_save_thentousends_obejcts() {
    fObjectStore.beginUpdate();
    
    for(int i = 0; i < 10000; ++i) {
      save(new Object());
    }
    
    long currentTime = System.nanoTime();
    fObjectStore.endUpdate();
    long durationInMs = (System.nanoTime() - currentTime) / 1000000;
    assertTrue("10'000 took " +durationInMs, durationInMs < 500);
  }

  @Override
  protected TestMode getTestMode() {
    return TestMode.filesystem;
  }
  
}
