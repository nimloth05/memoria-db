package org.memoriadb.test.core.query;

import java.util.List;

import org.memoriadb.core.*;
import org.memoriadb.test.core.testclasses.SimpleTestObj;

public class DataModeTest extends QueryTest {

  public void test_polymorph_query_does_not_work() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    List<Object> result = fStore.getAll(Object.class);
    assertEquals(0, result.size()); 
  }
  
  
  @Override
  protected void configureReopen(CreateConfig config) {
    setDBMode(DBMode.data);
    super.configureReopen(config);
  }

}
