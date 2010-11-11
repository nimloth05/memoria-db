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

package org.memoriadb.test.javaapi;

import junit.framework.TestCase;

import java.util.LinkedHashSet;
import java.util.Set;

public class LinkedSetTest extends TestCase {
  
  public void test_linked_HashSet() {
    Set<Integer> s1 = new LinkedHashSet<Integer>();
    s1.add(1);
    s1.add(2);

    Set<Integer> s2 = new LinkedHashSet<Integer>();
    s2.add(1);
    s2.add(2);

    Set<Integer> s3 = new LinkedHashSet<Integer>();
    s3.add(2);
    s3.add(1);
    
    assertTrue(s1.equals(s2));
    
    // WRONG!
    assertTrue(s1.equals(s3));
  }
}
