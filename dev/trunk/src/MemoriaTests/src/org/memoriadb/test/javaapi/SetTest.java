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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SetTest extends TestCase {
  
  public void test_hashCode() {
    Set<Integer> set = new HashSet<Integer>();
    
    for(int i = 0; i < 10; ++i) {
      set.add(i*3);
    }
    
    Set<Integer> set2 = new HashSet<Integer>();
    set2.addAll(set);
    
    assertEquals(set.hashCode(), set2.hashCode());
  }
  
  public void test_sets_in_sets_with_mixed_types_but_same_hashCode() {
    Set<Integer> subSet1 = new HashSet<Integer>();
    subSet1.add(1);
    subSet1.add(2);
    
    Set<Short> subSet2 = new HashSet<Short>();
    subSet2.add(Short.valueOf("1"));
    subSet2.add(Short.valueOf("2"));
    
    Set<Collection<?>> mainSet = new HashSet<Collection<?>>();
    mainSet.add(subSet1);
    mainSet.add(subSet2);
    
    assertEquals(subSet1.hashCode(), subSet2.hashCode());
    assertFalse(subSet1.equals(subSet2));
    
    assertEquals(2, mainSet.size());
  }
  
  public void test_with_multiple_empty_sets() {
    Set<Integer> subSet1 = new HashSet<Integer>();
    Set<Integer> subSet2 = new HashSet<Integer>();
    
    Set<Collection<?>> mainSet = new HashSet<Collection<?>>();
    mainSet.add(subSet1);
    mainSet.add(subSet2);
    
    assertEquals(1, mainSet.size());
  }
  
  public void test_with_multiple_sets() {
    Set<Integer> subSet1 = new HashSet<Integer>();
    subSet1.add(1);
    subSet1.add(2);
    subSet1.add(3);
    
    Set<Integer> subSet2 = new HashSet<Integer>();
    subSet2.add(10);
    subSet2.add(20);
    subSet2.add(30);
    
    Set<Collection<?>> mainSet = new HashSet<Collection<?>>();
    mainSet.add(subSet1);
    mainSet.add(subSet2);
    
    assertEquals(2, mainSet.size());
  }

}
