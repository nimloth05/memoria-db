package org.memoriadb.test.core.query;

import java.util.List;

import org.memoriadb.IFilter;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public abstract class QueryTest extends AbstractObjectStoreTest {
  
  public void test_getAll_by_class_literal() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    assertEquals(1, fStore.getAll(SimpleTestObj.class).size());
  }
  
  public void test_getAll_by_class_name() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    assertEquals(1, fStore.getAll(SimpleTestObj.class.getName()).size());
  }
  
  public void test_getAll_class_name_with_filter() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    List<Object> result = fStore.getAll(SimpleTestObj.class.getName(), new IFilter<Object>() {

      @Override
      public boolean accept(Object object) {
        return false;
      }
    });
    
    assertEquals(0, result.size());
  }

}
