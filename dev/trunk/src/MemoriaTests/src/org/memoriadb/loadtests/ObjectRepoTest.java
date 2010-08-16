/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.loadtests;

import junit.framework.TestCase;
import org.memoriadb.core.ObjectRepository;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.loong.LongId;
import org.memoriadb.id.loong.LongIdFactory;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.test.testclasses.WrongHashCode;

import java.util.*;

public class ObjectRepoTest extends TestCase {
  
  private ObjectRepository fRepo;
  
  /**
   * Tests, if the identityHashMap mapping survives a GC. Reason: Some people in the internet claim that System.identityHashCode
   * uses the heap-Address of the object. This can not be, because it would break the general hashCode contract: 
   * <q><br/><br/> 
   * Whenever it is invoked on the same object more than once during 
   * an execution of a Java application, the <tt>hashCode</tt> method 
   * must consistently return the same integer...</q>
   * 
   * Anmerkung: Die Heap-Adresse wird vergeben, die VM merkt sich aber in einem Flag, ob das Objekt verschoben wurde oder nicht. Wenn es verschoben
   * wurde, wird der Alte hashCode abkopiert (die IBM VM macht es so), msc
   * 
   * This tests tries to "proove" that.
   */
  public void test_identityHashCode() {
    Set<Object> expected = new HashSet<Object>();
    Map<Object, Integer> objects = new IdentityHashMap<Object, Integer>();
    int count = 600000;
    for(int i = 0; i < count; ++i) {
      Object key = new StringObject("Hallo Welt ");
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
    IObjectId memoriaClassId = new LongId(1);
    
    List<Object> objects = new ArrayList<Object>();
    for(int i = 0; i < 10000; ++i) {
      Object obj = new WrongHashCode();
      
      fRepo.add(obj, memoriaClassId);
      objects.add(obj);
    }
    
    for(Object obj: objects) {
      IObjectId id = fRepo.getExistingId(obj);
      Object obj2 = fRepo.getExistingObject(id);
      assertSame(obj, obj2);
      assertEquals(id, fRepo.getExistingId(obj2));
    }
  }
  
  @Override
  protected void setUp() {
    fRepo = new ObjectRepository(new LongIdFactory());
  }
  
}
 