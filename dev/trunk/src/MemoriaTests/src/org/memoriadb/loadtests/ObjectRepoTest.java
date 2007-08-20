package org.memoriadb.loadtests;

import java.util.*;

import junit.framework.TestCase;

import org.memoriadb.core.ObjectRepo;
import org.memoriadb.test.core.testclasses.*;

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
   * Anmerkung: Die Heap-Adresse wird vergeben, die VM merkt sich aber in einem Falg, ob das Objekt verschoben wurde oder nicht. Wenn es verschoben
   * wurde, wird der Alte hashCode abkopiert (die IBM VM macht es so), msc
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
      Object obj = new WrongHashCode();
      
      fRepo.add(obj);
      objects.add(obj);
    }
    
    for(Object obj: objects) {
      long id = fRepo.getObjectId(obj);
      Object obj2 = fRepo.getObject(id);
      assertSame(obj, obj2);
      assertEquals(id, fRepo.getObjectId(obj2));
    }
  }
  
  @Override
  protected void setUp() {
    fRepo = new ObjectRepo();
  }
  
}
 