package org.memoriadb.test.core;

import org.memoriadb.core.meta.IMetaClass;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.inheritance.*;


public class InheritanceTest extends AbstractObjectStoreTest {
  
  public void test_create_metaObjects() {
    B b = createB();
    save(b);
    assertkMetaObjectHierarchy(b);
  }

  public void test_save_inheritance_obj() {
    B b = createB();
    save(b);
    reopen();
    assertkMetaObjectHierarchy(b);
    
    B loadedB = getAll(B.class).get(0);
    assertB(b, loadedB);
  }

  public void test_save_object_which_super_class_has_a_object_ref() {
    C c = new C();
    c.fTestObj = new SimpleTestObj("1");
    c.setLong(1L);
    saveAll(c);
    
    reopen();
    
    C loadedC = getAll(C.class).get(0);
    assertEquals(c.fTestObj, loadedC.fTestObj);
  }
  
  public void test_save_super_type_first() {
    A a = new A();
    a.setInt(1);
    a.setLong(1l);
    
    B b  = createB();
    
    save(a);
    save(b);
    reopen();
    
    assertkMetaObjectHierarchy(b);
    
    B loadedB = getAll(B.class).get(0);
    assertB(b, loadedB);
  }
  
  private void assertB(B b, B loadedB) {
    assertEquals(b.fBoolean, loadedB.fBoolean);
    assertEquals(b.fString, loadedB.fString);
    assertEquals(b.getInt(), loadedB.getInt());
    assertEquals(b.getLong(), loadedB.getLong());
  }
  
  private void assertkMetaObjectHierarchy(B b) {
    IMetaClass metaClass = fStore.getMetaClass(b);
    
    assertEquals(metaClass.getJavaClass(), B.class);
    assertEquals(metaClass.getSuperClass().getJavaClass(), A.class);
    
    IMetaClass javaObjectMetaObject = metaClass.getSuperClass().getSuperClass();
    long id = fStore.getObjectId(javaObjectMetaObject);
    
    assertEquals(IMetaClass.JAVA_OBJECT_META_OBJECT_ID, id);
    assertEquals(javaObjectMetaObject.getJavaClass(), Object.class);
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
