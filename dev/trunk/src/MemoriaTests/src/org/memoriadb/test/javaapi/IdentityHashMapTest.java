/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.test.javaapi;

import junit.framework.TestCase;
import org.memoriadb.test.testclasses.WrongHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Test for the IdentityHashMap from the collection framework.
 * 
 * @author sandro
 *
 */
public class IdentityHashMapTest extends TestCase {
  
  private IdentityHashMap<Object, Object> fMap;

  public void test_containsKey() {
    WrongHashCode obj1 = new WrongHashCode();
    WrongHashCode obj2 = new WrongHashCode();
    fMap.put(obj1, "1");
    assertTrue(fMap.containsKey(obj1));
    assertFalse(fMap.containsKey(obj2));
  }
  
  public void test_containsKey_fromKeySet() {
    WrongHashCode obj1 = new WrongHashCode();
    WrongHashCode obj2 = new WrongHashCode();
    fMap.put(obj1, "1");
    Set<Object> keySet = fMap.keySet();
    assertTrue(keySet.contains(obj1));
    assertFalse(keySet.contains(obj2));
  }
  
  public void test_containsValue() {
    WrongHashCode obj1 = new WrongHashCode();
    fMap.put(obj1, "1");
    assertTrue(fMap.containsValue("1"));
    assertFalse(fMap.containsValue("2"));
  }
  
  public void test_keySet_removeAll() {
    WrongHashCode obj1 = new WrongHashCode();
    WrongHashCode obj2 = new WrongHashCode();
    fMap.put(obj1, "1");
    fMap.put(obj2, "2");
    
    Set<Object> keySet = fMap.keySet();
    
    Collection<Object> removeCollection = new ArrayList<Object>(2);
    removeCollection.add(new WrongHashCode());
    removeCollection.add(new WrongHashCode());
    
    assertFalse(keySet.containsAll(removeCollection));
    
    keySet.removeAll(removeCollection);
    assertEquals(0, fMap.size());
    
    try {
      assertTrue(keySet.addAll(removeCollection));
      fail("AddAll is not possible on a keySet");
    }
    catch (UnsupportedOperationException e) {
      //
    }
  }

  @Override
  protected void setUp() throws Exception {
    fMap = new IdentityHashMap<Object, Object>();
  }
  
}
