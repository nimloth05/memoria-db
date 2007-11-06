package org.memoriadb.test.core.crud;

import org.memoriadb.test.core.crud.testclass.*;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class BasicCrudTest extends AbstractObjectStoreTest {

  public void test_cyclic_reference() {
    Cyclic1 c1 = new Cyclic1("c1");
    Cyclic2 c2 = new Cyclic2("c2");
    
    c1.setC2(c2);
    c2.setC1(c1);
    
    long idc1 = save(c2, c1)[1];
    
    reopen();
    
    Cyclic1 c1_l1 = (Cyclic1) fStore.getObject(idc1);
    Cyclic2 c2_l1 = c1_l1.getC2();
    
    assertNotSame(c1, c1_l1);
    assertEquals(c1, c1_l1);

    assertNotSame(c2, c2_l1);
    assertEquals(c2, c2_l1);
    
    assertEquals(c1_l1, c2_l1.getC1());
    
    // replace c2 by c3
    Cyclic2 c3 = new Cyclic2("c3");
    c1_l1.setC2(c3);
    c3.setC1(c1_l1);
    
    save(c1_l1,c3);
   
    reopen();
    
    Cyclic1 c1_l2 = (Cyclic1) fStore.getObject(idc1);
    Cyclic2 c3_l1 = c1_l2.getC2();
     
    assertNotSame(c3, c3_l1);
    assertEquals(c3, c3_l1);
    assertEquals(c1_l2, c3.getC1());
  }
  
  public void test_save_attribute() {
    B b = new B("b");
    long id = fStore.save(b);
    
    reopen();
    
    B b_l1 = (B) fStore.getObject(id);
    assertNotSame(b_l1, b);
    assertEquals("b", b_l1.getName());
  }
  
  // FIXME geht noch nicht, msc
//  public void test_save_null_attribute() {
//    B b = new B(null);
//    long id = fStore.save(b);
//    
//    reopen();
//    
//    B bb = (B) fStore.getObject(id);
//    assertNull(bb.getName());
//  }
  
  // FIXME test einkommentieren....
//  public void test_save_null_reference() {
//    A a = new A(null);
//    long id = fStore.save(a);
//    
//    reopen();
//    
//    A a_l1 = (A) fStore.getObject(id);
//    assertNull(a_l1.getB());
//  }
  
  public void test_save_reference() {
    B b = new B("b");
    A a = new A(b);
    long id = fStore.save(b,a)[1];
    
    reopen();
    
    A a_l1 = (A) fStore.getObject(id);
    assertEquals("b", a_l1.getB().getName());
  }
  
  public void test_save_same_object_twice() {
    B b = new B("b");
    long id = fStore.save(b,b)[1];
    
    reopen();
    
    B b_l1 = (B) fStore.getObject(id);
    assertEquals("b", b_l1.getName());
  }
  
  // FIXME geht noch nicht, msc
//  public void test_save_unsaved_reference() {
//    A a = new A(new B("b")); // b is not saved
//    long id = fStore.save(a);
//    
//    reopen();
//    
//    A ab = (A) fStore.getObject(id);
//    assertNull(ab.getB());
//  }
  
  public void test_update_attribute() {
    B b = new B("b");
    long idb = fStore.save(b);
    b.setName("bb");
    long idbb = fStore.save(b);
    assertEquals(idb, idbb);
    
    reopen();
    
    B b_l1 = (B) fStore.getObject(idbb);
    assertEquals("bb", b_l1.getName());
  }
  
}
