package org.memoriadb.test.core;

import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.test.testclasses.inheritance.A;
import org.memoriadb.test.testclasses.inheritance.B;
import org.memoriadb.test.testclasses.inheritance.C;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.ArrayList;


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
    b.setTestObj(new StringObject());
    saveAll(b);
    
    assertTypeHierachy(B.class);
    reopen();
    assertTypeHierachy(B.class);
    
    B loadedB = query(B.class).get(0); 
    assertB(b, loadedB);
  }

  public void test_save_object_which_super_class_has_a_object_ref() {
    C c = new C();
    c.setTestObj(new StringObject("1"));
    c.setLong(1L);
    saveAll(c);
    
    reopen();
    
    C loadedC = query(C.class).get(0);
    assertEquals(c.getTestObj(), loadedC.getTestObj());
  }
  
  public void test_save_super_type_first() {
    A a = new A();
    a.setInt(1);
    a.setLong(1l);
    
    B b  = createB();
    b.setTestObj(new StringObject());
    
    save(a);
    saveAll(b);
    
    reopen();
    
    assertTypeHierachy(B.class);
    
    B loadedB = query(B.class).get(0);
    assertB(b, loadedB);
  }
  
  private void assertB(B b, B loadedB) {
    assertEquals(b.isBoolean(), loadedB.isBoolean());
    assertEquals(b.getString(), loadedB.getString());
    assertEquals(b.getInt(), loadedB.getInt());
    assertEquals(b.getLong(), loadedB.getLong());
  }
  
  private B createB() {
    B b = new B();
    b.setBoolean(true);
    b.setString("1");
    b.setInt(1);
    b.setLong(1L);
    return b;
  }

}
