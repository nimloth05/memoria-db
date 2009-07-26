package org.memoriadb.test.core.query;

import org.memoriadb.IFilter;
import org.memoriadb.IFilterControl;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.List;

public class BasicQueryTest extends AbstractMemoriaTest {
  
  public void test_getAll_by_class_literal() {
    StringObject obj = new StringObject("1");
    save(obj);
    
    reopen();
    
    assertEquals(1, fObjectStore.query(StringObject.class).size());
  }
  
  public void test_getAll_by_class_name() {
    StringObject obj = new StringObject("1");
    save(obj);
    
    reopen();
    
    assertEquals(1, fObjectStore.query(StringObject.class.getName()).size());
  }
  
  public void test_getAll_class_name_with_filter() {
    StringObject obj = new StringObject("1");
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
