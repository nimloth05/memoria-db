package bootstrap.core;

import java.util.*;

import bootstrap.core.testclasses.*;

public class LoadTest extends AbstractFileStoreTest {
  
  
  public void test_() {
    int count = 10;
    int failed = 0;
    int success = 0;
    
    for(int i = 0; i < count; ++i) {
      try {
        super.setUp();
        test_save_objectref();
        ++success;
        Thread.sleep(1000);
      }
      catch (Exception e) {
        ++failed;
      }
    }
    assertEquals("Test failed " + failed + " times", count, success);
  }
  
  
  @SuppressWarnings("nls")
  public void test_save_objectref() {
    List<Object> objects = new ArrayList<Object>();
    
    for(int i = 0; i < 15000; ++i) {
      Referencer composite = new Referencer();
      composite.set("1");
      objects.add(composite);
    }
    
    save(objects.toArray());
    fStore.checkSanity();
    
    List<Referencer> allSavedObjects = getAll(Referencer.class);
    for(Referencer ref: allSavedObjects) {
      Object obj = ref.get();
      long objectId = fStore.getObjectId(obj);
      assertSame("id collision: "+objectId, obj, fStore.getObejctById(objectId));
    }
    
    reopen();
    
    Referencer composite = getAll(Referencer.class).get(0);
    TestObj obj = composite.get(); 
    assertNotNull("1", obj.getString());
  }
  
  //FIXME: Test must be extendend
  public void test_write_object() {
    List<Object> objects = new ArrayList<Object>();
    for(int i = 0; i < 5; ++i) {
      objects.add(new TestObj("Hallo Welt "+i, i));
    }
    
    save(objects.toArray());
    
    reopen();
    
    TestObj obj = (TestObj) fStore.getObejctById(2);
    assertEquals("Hallo Welt 0", obj.getString());
    assertEquals(0, obj.getI());
  }
  
}
