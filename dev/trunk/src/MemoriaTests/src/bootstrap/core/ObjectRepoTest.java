package bootstrap.core;

import java.util.*;

import bootstrap.core.testclasses.TestObj;
import junit.framework.TestCase;

public class ObjectRepoTest extends TestCase {
  
  private ObjectRepo fRepo;
  
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
  
  public void test_stress() {
    List<Object> objects = new ArrayList<Object>();
    for(int i = 0; i < 20000; ++i) {
      TestObj obj = new TestObj();
      fRepo.register(obj);
      objects.add(obj);
    }
    
    for(Object obj: objects) {
      long id = fRepo.getObjectId(obj);
      Object obj2 = fRepo.getObjectById(id);
      assertEquals(System.identityHashCode(obj), System.identityHashCode(obj2));
      assertSame("?",  obj, obj2);
      assertEquals(id, fRepo.getObjectId(obj2));
    }
  }
  
  @Override
  protected void setUp() {
    fRepo = new ObjectRepo();
  }
  
}
 