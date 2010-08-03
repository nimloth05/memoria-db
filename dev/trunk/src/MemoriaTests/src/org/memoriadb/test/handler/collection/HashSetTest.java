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

package org.memoriadb.test.handler.collection;

import java.util.*;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.HashSetKey;


public class HashSetTest extends SetTest {

  /**
   * This tests fails when the binding is not performed in two steps 
   * (generation one and generation two bindings).
   */
  public void test_key_with_reference() {
    Set<HashSetKey> set1 = new HashSet<HashSetKey>();
    Set<HashSetKey> set2 = new HashSet<HashSetKey>();
    
    final int count = 10;
    
    for(int i = 0; i < count; ++i) {
      set1.add(new HashSetKey(Integer.toString(i)));
      set2.add(new HashSetKey(Integer.toString(i)));
    }
    
    IObjectId id1 = saveAll(set1);
    IObjectId id2 = saveAll(set2);
    
    reopen();
    
    set1 = get(id1);
    set2 = get(id2);
    
    assertEquals(count, set1.size());
    assertEquals(count, set2.size());
  }
  
  @Override
  public void test_list_in_list() {
    // sets in sets cannot be serialized
  }
  
  @Override
  public void test_list_in_list_in_multiple_save_calls() {
    // sets in sets cannot be serialized    
  }
  
  @Override
  protected <T> Collection<T> createCollection() {
    return new HashSet<T>();
  }

}
