package bootstrap.core;

import bootstrap.core.testclasses.TestObj;
import junit.framework.TestCase;

public class ObjectRepoTest extends TestCase {
  
  private ObjectRepo fRepo;
  
  @Override
  protected void setUp() {
    fRepo = new ObjectRepo();
  }

  public void test_put_new_object_in_cache() {
    TestObj obj = new TestObj();
    long id = fRepo.register(obj);
    Object obj2 = fRepo.getObjectById(id);
    assertSame(obj, obj2);
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
  
  public void test_put_object_with_id_in_cache() {
    TestObj obj = new TestObj();
    fRepo.put(20, obj);
    
    TestObj obj2 = new TestObj();
    long id = fRepo.register(obj2);
    assertEquals(21, id);
    
    assertEquals(obj, fRepo.getObjectById(20));
    assertEquals(obj2, fRepo.getObjectById(id));
    
  }
  
}
