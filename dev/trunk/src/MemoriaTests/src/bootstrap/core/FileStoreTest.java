package bootstrap.core;

import java.io.File;
import java.util.*;

import junit.framework.TestCase;
import bootstrap.core.testclasses.*;

public class FileStoreTest extends TestCase {
  
  
  private File fFile;
  
  @Override
  protected void setUp() {
    fFile = new File("fileStore.db");
    fFile.delete();
  }
  
  public void test_write_object() {
    List<Object> objects = new ArrayList<Object>();
    for(int i = 0; i < 1; ++i) {
      objects.add(new TestObj("Hallo Welt "+i, i));
    }
    
    FileStore store = new FileStore(fFile);
    store.writeObject(objects);
    
    store = new FileStore(fFile);
    store.open();
    
    TestObj obj = (TestObj) store.getObejctById(2);
    assertEquals("Hallo Welt 0", obj.getString());
    assertEquals(0, obj.getI());
  }
  
  @SuppressWarnings("nls")
  public void test_save_objectref() {
    List<Object> objects = new ArrayList<Object>();
    for(int i = 0; i < 10000; ++i) {
      Composite composite = new Composite();
      composite.set("1");
      objects.add(composite.get());
      objects.add(composite);
    }
    
    FileStore store = new FileStore(fFile);
    store.writeObject(objects);
    
    store = new FileStore(fFile);
    store.open();
    Composite composite = (Composite) store.getObejctById(4);
    TestObj obj = composite.get(); 
    assertNotNull("1", obj.getString());
  }
  
  public void test_hydration() {
    FileStore store = new FileStore(fFile);
    store.writeObject(new A(1, "a1"), new A(2, "a2"));
    
    store = new FileStore(fFile);
    store.open();
  }
}
