package bootstrap.core;

import java.util.*;

import bootstrap.core.testclasses.*;
import junit.framework.TestCase;

public class ObjectRepoTest extends TestCase {
  
  private ObjectRepo fRepo;
  
  /**
   * Tests, if the identityHashMap mapping survives a GC. Reason: Some people in the internet claim that System.identityHashCode
   * uses the heap-Address of the object. This can not be, because it would break the general hashCode contract: 
   * <q><br/><br/> 
   * Whenever it is invoked on the same object more than once during 
   * an execution of a Java application, the <tt>hashCode</tt> method 
   * must consistently return the same integer...</q>
   * 
   * This tests tries to "proove" that.
   */
  public void test_identityHashCode() {
    Set<Object> expected = new HashSet<Object>();
    Map<Object, Integer> objects = new IdentityHashMap<Object, Integer>();
    int count = 600000;
    for(int i = 0; i < count; ++i) {
      Object key = new TestObj("Hallo Welt ", i);
      objects.put(key, i);
      expected.add(key);
    }
    
    System.gc();
    
    Iterator<Object> keys = objects.keySet().iterator();
    for(int i = 0; keys.hasNext(); ++i) {
      Object key = keys.next(); 
      if ((i % 4) == 0)   continue;
      keys.remove();
      expected.remove(key);
    }
    
    System.gc();
    
    for(Object key: expected) {
      assertTrue(objects.containsKey(key));
    }
  }

  public void test_put_a_lot_of_objects() {
    List<Object> objects = new ArrayList<Object>();
    for(int i = 0; i < 100000; ++i) {
      Object obj = new WrongHashCodeTestObj();
      fRepo.register(obj);
      objects.add(obj);
    }
    
    for(Object obj: objects) {
      long id = fRepo.getObjectId(obj);
      Object obj2 = fRepo.getObjectById(id);
      assertSame(obj, obj2);
      assertEquals(id, fRepo.getObjectId(obj2));
    }
  }
  
  public void test_put_meta_object_in_cache() {
    MetaClass classObject = new MetaClass(TestObj.class);
    long id = fRepo.register(classObject);
    
    assertSame(classObject, fRepo.getObjectById(id));
    assertSame(classObject, fRepo.getMetaObject(TestObj.class));
  }
  
  public void test_put_meta_object_with_id_in_cache() {
    MetaClass classObject = new MetaClass(TestObj.class);
    fRepo.put(20, classObject);
    
    assertSame(classObject, fRepo.getObjectById(20));
    assertSame(classObject, fRepo.getMetaObject(TestObj.class));
  }
  
  public void test_put_new_object_in_cache() {
    TestObj obj = new TestObj();
    long id = fRepo.register(obj);
    Object obj2 = fRepo.getObjectById(id);
    assertSame(obj, obj2);
  }
  
  public void test_put_object_with_id_in_cache() {
    TestObj obj = new TestObj();
    fRepo.put(20, obj);
    
    TestObj obj2 = new TestObj();
    long id = fRepo.register(obj2);
    assertEquals(21, id);
    
    assertEquals(obj, fRepo.getObjectById(20));
    assertEquals(obj2, fRepo.getObjectById(id));
  }
  
  @Override
  protected void setUp() {
    fRepo = new ObjectRepo();
  }
  
}
 