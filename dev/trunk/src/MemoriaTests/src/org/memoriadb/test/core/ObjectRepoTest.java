package org.memoriadb.test.core;

import junit.framework.TestCase;

import org.memoriadb.core.ObjectRepo;
import org.memoriadb.core.meta.*;
import org.memoriadb.test.core.testclasses.TestObj;

public class ObjectRepoTest extends TestCase {
  
  private ObjectRepo fRepo;
  
  public void test_put_inheritence_object() {
    
  }
  
  public void test_put_meta_object_in_cache() {
    IMetaClass classObject = new MetaClass(TestObj.class);
    long id = fRepo.add(classObject);
    
    assertSame(classObject, fRepo.getObject(id));
    assertSame(classObject, fRepo.getMetaClass(TestObj.class));
  }
  
  public void test_put_meta_object_with_id_in_cache() {
    IMetaClass classObject = new MetaClass(TestObj.class);
    fRepo.add(20, classObject, 0);
    
    assertSame(classObject, fRepo.getObject(20));
    assertSame(classObject, fRepo.getMetaClass(TestObj.class));
  }
  
  public void test_put_new_object_in_cache() {
    TestObj obj = new TestObj();
    long id = fRepo.add(obj);
    Object obj2 = fRepo.getObject(id);
    assertSame(obj, obj2);
  }
  
  public void test_put_object_with_id_in_cache() {
    TestObj obj = new TestObj();
    fRepo.add(20, obj, 0);
    
    TestObj obj2 = new TestObj();
    long id = fRepo.add(obj2);
    assertEquals(21, id);
    
    assertEquals(obj, fRepo.getObject(20));
    assertEquals(obj2, fRepo.getObject(id));
  }
  
  @Override
  protected void setUp() {
    fRepo = new ObjectRepo();
  }
  
}
 