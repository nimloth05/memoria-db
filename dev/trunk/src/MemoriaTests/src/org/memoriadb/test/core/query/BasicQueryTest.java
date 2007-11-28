package org.memoriadb.test.core.query;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.test.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class BasicQueryTest extends AbstractMemoriaTest {
  
  public void test_getAll_by_class_literal() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    assertEquals(1, fObjectStore.query(SimpleTestObj.class).size());
  }
  
  public void test_getAll_by_class_name() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    assertEquals(1, fObjectStore.query(SimpleTestObj.class.getName()).size());
  }
  
  public void test_getAll_class_name_with_filter() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    List<Object> result = fObjectStore.query(Object.class, new IFilter<Object>() {

      @Override
      public boolean accept(Object object, IFilterControl control) {
        return false;
      }
    });
    
    assertEquals(0, result.size());
  }

}
