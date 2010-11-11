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

package org.memoriadb.test.core.query;

import org.memoriadb.test.testclasses.inheritance.A;
import org.memoriadb.test.testclasses.inheritance.B;
import org.memoriadb.test.testclasses.inheritance.C;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.AbstractList;
import java.util.ArrayList;

public class DataModeQueryTest extends AbstractMemoriaTest {

  public void test_abstract_class() {
    // AbstractList
  }
  
  public void test_empty_user_space() {
    reopenDataMode();
    
    assertEquals(0, fDataStore.query(Object.class.getName()).size());
  }
  
  public void test_polymorph_query() {
    A a = new A();
    B b = new B();
    C c = new C();
    
    save(a);
    save(b);
    save(c);
    
    save(new ArrayList<Object>());
    
    reopenDataMode();
    
    assertEquals(1, fDataStore.query(AbstractList.class.getName()).size());
    
    assertEquals(4, fDataStore.query(Object.class.getName()).size()); 
  }
  

}
