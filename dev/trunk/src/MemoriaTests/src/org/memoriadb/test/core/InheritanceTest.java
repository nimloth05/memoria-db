package org.memoriadb.test.core;

import java.util.ArrayList;

import org.memoriadb.test.testclasses.SimpleTestObj;
import org.memoriadb.test.testclasses.inheritance.*;
import org.memoriadb.testutil.AbstractMemoriaTest;


public class InheritanceTest extends AbstractMemoriaTest {
  
  public void test_create_metaObjects() {
    B b = createB();
    save(b);
    assertTypeHierachy(B.class);
  }
  
  public void test_handler_hierachy() {
    assertTypeHierachy(ArrayList.class);
    reopen();
    assertTypeHierachy(ArrayList.class);
  }
  
  public void test_memoriaClass_for_Object_exists() {
    assertEquals(Object.class.getName(), fObjectStore.getTypeInfo().getMemoriaClass(Object.class).getJavaClassName());
  }

  public void test_save_inheritance_obj() {
    B b = createB();
    b.fTestObj = new SimpleTestObj();
    saveAll(b);
    
    assertTypeHierachy(B.class);
    reopen();
    assertTypeHierachy(B.class);
    
    B loadedB = query(B.class).get(0); 
    assertB(b, loadedB);
  }

  public void test_save_object_which_super_class_has_a_object_ref() {
    C c = new C();
    c.fTestObj = new SimpleTestObj("1");
    c.setLong(1L);
    saveAll(c);
    
    reopen();
    
    C loadedC = query(C.class).get(0);
    assertEquals(c.fTestObj, loadedC.fTestObj);
  }
  
  public void test_save_super_type_first() {
    A a = new A();
    a.setInt(1);
    a.setLong(1l);
    
    B b  = createB();
    b.fTestObj = new SimpleTestObj();
    
    save(a);
    saveAll(b);
    
    reopen();
    
    assertTypeHierachy(B.class);
    
    B loadedB = query(B.class).get(0);
    assertB(b, loadedB);
  }
  
  private void assertB(B b, B loadedB) {
    assertEquals(b.fBoolean, loadedB.fBoolean);
    assertEquals(b.fString, loadedB.fString);
    assertEquals(b.getInt(), loadedB.getInt());
    assertEquals(b.getLong(), loadedB.getLong());
  }
  
  private B createB() {
    B b = new B();
    b.fBoolean = true;
    b.fString = "1";
    b.setInt(1);
    b.setLong(1L);
    return b;
  }

}
