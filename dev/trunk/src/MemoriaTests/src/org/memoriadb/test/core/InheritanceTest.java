package org.memoriadb.test.core;

import java.util.ArrayList;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.inheritance.*;
import org.memoriadb.testutil.AbstractMemoriaTest;


public class InheritanceTest extends AbstractMemoriaTest {
  
  public void test_create_metaObjects() {
    B b = createB();
    save(b);
    assertMetaObjectHierarchy(b);
  }
  
  public void test_handler_hierachy() {
    assertTypeHierachy(ArrayList.class);
//    IMemoriaClass arrayListClass = fObjectStore.getTypeInfo().getMemoriaClass(ArrayList.class);
//    assertNotNull(arrayListClass.getSuperClass());
//    
//    IMemoriaClass abstractListClass = arrayListClass.getSuperClass();
//    assertNotNull(abstractListClass);
//    assertSame(abstractListClass, fObjectStore.getTypeInfo().getMemoriaClass(AbstractList.class));
//    assertEquals(ReflectionUtil.getClass(abstractListClass.getJavaClassName()), AbstractList.class);
//    
//    IMemoriaClass abstractCollectionClass = abstractListClass.getSuperClass();
//    assertNotNull(abstractCollectionClass);
//    
//    IMemoriaClass objectClass = abstractCollectionClass.getSuperClass();
//    assertNotNull(objectClass);
  }
  
  public void test_memoriaClass_for_Object_exists() {
    assertEquals(Object.class.getName(), fObjectStore.getTypeInfo().getMemoriaClass(Object.class).getJavaClassName());
  }

  public void test_save_inheritance_obj() {
    B b = createB();
    b.fTestObj = new SimpleTestObj();
    IObjectId id = saveAll(b);
    
    assertMetaObjectHierarchy(b);
    reopen();
    assertMetaObjectHierarchy((B)get(id));
    
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
    IObjectId idB = saveAll(b);
    
    reopen();
    
    assertMetaObjectHierarchy((B)get(idB));
    
    B loadedB = query(B.class).get(0);
    assertB(b, loadedB);
  }
  
  private void assertB(B b, B loadedB) {
    assertEquals(b.fBoolean, loadedB.fBoolean);
    assertEquals(b.fString, loadedB.fString);
    assertEquals(b.getInt(), loadedB.getInt());
    assertEquals(b.getLong(), loadedB.getLong());
  }
  
  private void assertMetaObjectHierarchy(B b) {
    IMemoriaClass metaClass = fObjectStore.getTypeInfo().getMemoriaClass(b);
    IMemoriaClass objectClass = fObjectStore.getTypeInfo().getMemoriaClass(Object.class);
    
    assertEquals(metaClass.getJavaClassName(), B.class.getName());
    assertEquals(metaClass.getSuperClass().getJavaClassName(), A.class.getName());
    
    IMemoriaClass javaObjectMetaObject = metaClass.getSuperClass().getSuperClass();
    IObjectId id = fObjectStore.getId(javaObjectMetaObject);
    
    assertEquals(fObjectStore.getId(objectClass), id);
    assertEquals(javaObjectMetaObject.getJavaClassName(), Object.class.getName());
  }
  
  private void assertTypeHierachy(Class<?> clazz) {
    if (clazz == null) return;
    
    IMemoriaClass memoriaClass = fObjectStore.getTypeInfo().getMemoriaClass(clazz);
    IMemoriaClass superClass = memoriaClass.getSuperClass();
    assertNotNull(superClass);
    assertSame(superClass, fObjectStore.getTypeInfo().getMemoriaClass(clazz.getSuperclass().getName()));
    assertSame(ReflectionUtil.getClass(superClass.getJavaClassName()), clazz.getSuperclass());

    assertTypeHierachy(clazz.getSuperclass());
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
