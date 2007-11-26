package org.memoriadb.test.core.query;

import java.util.List;

import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.test.core.testclasses.SimpleTestObj;

public class DataModeQueryTest extends QueryTest {

  public void test_polymorph_query_does_not_work() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopenDataMode();
    
    List<IDataObject> result = fDataStore.query(Object.class.getName());
    
    assertEquals(1, result.size()); 
  }

}
