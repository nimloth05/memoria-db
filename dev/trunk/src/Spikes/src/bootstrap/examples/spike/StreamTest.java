package bootstrap.examples.spike;

import java.io.File;
import java.util.*;

import junit.framework.TestCase;

public class StreamTest extends TestCase {
  
  
  public void test_write_read() throws Exception {
    List<Object> objects = new ArrayList<Object>();
    for(int i = 0; i < 50000; ++i) {
      objects.add(new TestObj("Hallo Welt "+i, i));
    }
    
    
    File file = new File("test.db");
    
    ObjectOutStream stream = new ObjectOutStream(file);
    for(Object obj: objects) {
      stream.write(obj);
    }
    stream.flush();
    stream.close();
    
    
    ObjectInStream inStream = new ObjectInStream(file);
    List<Object> allObjs = inStream.readAllObejcts();
    inStream.close();
    
    assertEquals(objects.size(), allObjs.size());
    for(int i = 0; i < allObjs.size(); ++i) {
      TestObj oriObj = (TestObj) allObjs.get(i);
      TestObj readObj = (TestObj) allObjs.get(i);
      assertEquals(oriObj.getI(), readObj.getI());
      assertEquals(oriObj.getString(), readObj.getString());
    }
  }

}
