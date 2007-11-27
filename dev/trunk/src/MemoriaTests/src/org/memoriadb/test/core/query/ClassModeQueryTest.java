package org.memoriadb.test.core.query;

import java.util.*;

import org.memoriadb.*;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.inheritance.*;
import org.memoriadb.testutil.AbstractMemoriaTest;


/**
 * 
 * @author sandro
 *
 */
public class ClassModeQueryTest extends AbstractMemoriaTest {
  
  public void test_getAll_class_literal_with_filter() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    List<SimpleTestObj> result = fObjectStore.query(SimpleTestObj.class, new IFilter<SimpleTestObj>() {

      @Override
      public boolean accept(SimpleTestObj object, IFilterControl control) {
        return false;
      }
    });
    
    assertEquals(0, result.size());
  }
  
  public void test_memoria_class() {
    List<IMemoriaClass> query = fObjectStore.query(IMemoriaClass.class);
    assertEquals(0, query.size());
  }
  
  public void test_polymorph_query() {
    SimpleTestObj obj = new SimpleTestObj("1");
    save(obj);
    
    reopen();
    
    List<Object> result = fObjectStore.query(Object.class);
    assertEquals(fObjectStore.getAllObjects().size(), result.size());
  }
  
  public void test_polymorph_query_with_field_objects() {
    fObjectStore.beginUpdate();
    
    save(new A());
    save(new B());
    save(new C());
    
    fObjectStore.endUpdate();
        
  }
  
  public void test_query_abstract_classes() {
    fObjectStore.save(new ArrayList<Object>());
    List<AbstractList> query = fObjectStore.query(AbstractList.class);
    assertEquals(1, query.size());
  }
  
}
