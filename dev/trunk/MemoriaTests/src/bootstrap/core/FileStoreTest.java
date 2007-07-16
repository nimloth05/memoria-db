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
    for(int i = 0; i < 50000; ++i) {
      objects.add(new TestObj("Hallo Welt "+i, i));
    }
    
    FileStore store = new FileStore(fFile);
    store.writeObject(objects);
    
    store = new FileStore(fFile);
    store.open();
    
//    ObjectInStream inStream = new ObjectInStream(file);
//    List<Object> allObjs = inStream.readAllObejcts();
//    inStream.close();
//    
//    assertEquals(objects.size(), allObjs.size());
//    for(int i = 0; i < allObjs.size(); ++i) {
//      TestObj oriObj = (TestObj) allObjs.get(i);
//      TestObj readObj = (TestObj) allObjs.get(i);
//      assertEquals(oriObj.getI(), readObj.getI());
//      assertEquals(oriObj.getString(), readObj.getString());
//    }
  }
  
  public void test_hydration() {
    FileStore store = new FileStore(fFile);
    store.writeObject(new A(1, "a"), new A(1, "a"));
    
    store = new FileStore(fFile);
    store.open();
    
    //System.out.println(store.getHydratedObjects());
  }
  

}
