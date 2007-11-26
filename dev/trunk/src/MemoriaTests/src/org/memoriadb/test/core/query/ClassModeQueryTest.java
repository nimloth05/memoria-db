package org.memoriadb.test.core.query;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractMemoriaTest;


/**
 * Nothing to do, because the default is {@link DBMode#clazz}
 * @author sandro
 *
 */
public class ClassModeQueryTest extends AbstractMemoriaTest {
  
  public void test_getAll_class_literal_with_filter() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    List<SimpleTestObj> result = fObjectStore.query(new IFilter<SimpleTestObj>() {

      @Override
      public boolean accept(SimpleTestObj object, IFilterControl control) {
        return false;
      }
    });
    
    assertEquals(0, result.size());
  }
  
  public void test_polymorph_query() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    List<Object> result = fObjectStore.query(Object.class);
    assertEquals(fObjectStore.getAllObjects().size(), result.size());
  }
  
}
