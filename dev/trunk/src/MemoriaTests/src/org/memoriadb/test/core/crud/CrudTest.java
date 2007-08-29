package org.memoriadb.test.core.crud;

import org.memoriadb.test.core.crud.testclass.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class CrudTest extends AbstractObjectStoreTest {

  public void test_cyclic_reference() {
    C1 c1 = new C1("c1");
    C2 c2 = new C2("c2");
    
    c1.setC2(c2);
    c2.setC1(c1);
    
    long idc1 = save(c2, c1);
    
    reopen();
    
    C1 c1b = (C1) fStore.getObject(idc1);
    C2 c2b = c1b.getC2();
    
    assertNotSame(c1, c1b);
    assertEquals(c1, c1b);

    assertNotSame(c2, c2b);
    assertEquals(c2, c2b);
    
    assertEquals(c1b, c2b.getC1());
    
    // replace c2 by c22
    C2 c22 = new C2("c22");
    c1b.setC2(c22);
    c22.setC1(c1b);
    
    save(c1b,c22);
   
    reopen();
    
    C1 c1c = (C1) fStore.getObject(idc1);
    C2 c22b = c1c.getC2();
    
    assertNotSame(c22, c22b);
    assertEquals(c22, c22b);
    assertEquals(c1c, c22.getC1());
  }
  
  public void test_save_attribute() {
    B b = new B("b");
    long id = fStore.save(b);
    
    reopen();
    
    B bb = (B) fStore.getObject(id);
    assertNotSame(bb, b);
    assertEquals("b", bb.getName());
  }
  
  // FIXME geht noch nicht, msc
  public void test_save_null_attribute() {
    B b = new B(null);
    long id = fStore.save(b);
    
    reopen();
    
    B bb = (B) fStore.getObject(id);
    assertNull(bb.getName());
  }
  
  public void test_save_null_reference() {
    A a = new A(null);
    long id = fStore.save(a);
    
    reopen();
    
    A ab = (A) fStore.getObject(id);
    assertNull(ab.getB());
  }
  
  public void test_save_reference() {
    B b = new B("b");
    A a = new A(b);
    long id = fStore.save(b,a);
    
    reopen();
    
    A ab = (A) fStore.getObject(id);
    assertEquals("b", ab.getB().getName());
  }
  
  public void test_save_same_object_twice() {
    B b = new B("b");
    long id = fStore.save(b,b);
    
    reopen();
    
    B bb = (B) fStore.getObject(id);
    assertEquals("b", bb.getName());
  }
  
  // FIXME geht noch nicht, msc
  public void test_save_unsaved_reference() {
    A a = new A(new B("b")); // b is not saved
    long id = fStore.save(a);
    
    reopen();
    
    A ab = (A) fStore.getObject(id);
    assertNull(ab.getB());
  }
  
  public void test_update_attribute() {
    B b = new B("b");
    long idb = fStore.save(b);
    b.setName("bb");
    long idbb = fStore.save(b);
    assertEquals(idb, idbb);
    
    reopen();
    
    B bb = (B) fStore.getObject(idbb);
    assertEquals("bb", bb.getName());
  }
  
}
