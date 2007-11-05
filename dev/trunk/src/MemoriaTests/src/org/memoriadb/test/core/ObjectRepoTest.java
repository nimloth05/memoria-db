package org.memoriadb.test.core;

import junit.framework.TestCase;

import org.memoriadb.core.ObjectRepo;
import org.memoriadb.core.meta.*;
import org.memoriadb.test.core.testclasses.SimpleTestObj;

public class ObjectRepoTest extends TestCase {
  
  private ObjectRepo fRepo;
  
  public void test_deleted_object_is_not_contained() {
    SimpleTestObj obj = new SimpleTestObj();
    long id = fRepo.add(obj);
    
    fRepo.delete(obj);
    
    assertFalse(fRepo.contains(obj));
    assertFalse(fRepo.contains(id));
    
  }
  
  public void test_put_inheritence_object() {
    
  }
  
  public void test_put_meta_object_in_cache() {
    IMemoriaClass classObject = new MemoriaFieldClass(SimpleTestObj.class);
    long id = fRepo.add(classObject);
    
    assertSame(classObject, fRepo.getObject(id));
    assertSame(classObject, fRepo.getMemoriaClass(SimpleTestObj.class));
  }
  
  public void test_put_meta_object_with_id_in_cache() {
    IMemoriaClass classObject = new MemoriaFieldClass(SimpleTestObj.class);
    fRepo.add(20, classObject, 0, 0);
    
    assertSame(classObject, fRepo.getObject(20));
    assertSame(classObject, fRepo.getMemoriaClass(SimpleTestObj.class));
  }
  
  public void test_put_new_object_in_cache() {
    SimpleTestObj obj = new SimpleTestObj();
    long id = fRepo.add(obj);
    Object obj2 = fRepo.getObject(id);
    assertSame(obj, obj2);
  }
  
  public void test_put_object_with_id_in_cache() {
    SimpleTestObj obj = new SimpleTestObj();
    //Wir starten hier absichtlich mit 20.
    fRepo.add(20, obj, 0, 0);
    
    SimpleTestObj obj2 = new SimpleTestObj();
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
 