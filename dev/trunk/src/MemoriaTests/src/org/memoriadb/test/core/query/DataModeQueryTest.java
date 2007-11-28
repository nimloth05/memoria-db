package org.memoriadb.test.core.query;

import java.util.*;

import org.memoriadb.test.testclasses.inheritance.*;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class DataModeQueryTest extends AbstractMemoriaTest {

  public void test_abstract_class() {
    // AbstractList
  }
  
  public void test_empty_user_space() {
    reopenDataMode();
    
    assertEquals(0, fDataStore.query(Object.class.getName()).size());
  }
  
  public void test_polymorph_query() {
    A a = new A();
    B b = new B();
    C c = new C();
    
    save(a);
    save(b);
    save(c);
    
    save(new ArrayList<Object>());
    
    reopenDataMode();
    
    assertEquals(1, fDataStore.query(AbstractList.class.getName()).size());
    
    assertEquals(4, fDataStore.query(Object.class.getName()).size()); 
  }
  

}
