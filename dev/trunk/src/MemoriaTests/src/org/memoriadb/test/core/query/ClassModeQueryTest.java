package org.memoriadb.test.core.query;

import java.util.List;

import org.memoriadb.IFilter;
import org.memoriadb.core.DBMode;
import org.memoriadb.test.core.testclasses.SimpleTestObj;


/**
 * Nothing to do, because the default is {@link DBMode#clazz}
 * @author nienor
 *
 */
public class ClassModeQueryTest extends QueryTest {
  
  public void test_getAll_class_literal_with_filter() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    List<SimpleTestObj> result = fStore.getAll(SimpleTestObj.class, new IFilter<SimpleTestObj>() {

      @Override
      public boolean accept(SimpleTestObj object) {
        return false;
      }
    });
    
    assertEquals(0, result.size());
  }
  
  public void test_polymorph_query() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    List<Object> result = fStore.getAll(Object.class);
    assertEquals(fStore.getAllObjects().size(), result.size());
  }
  
}
