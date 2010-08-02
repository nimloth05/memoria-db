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

package org.memoriadb.test.core.query;

import org.memoriadb.IFilter;
import org.memoriadb.IFilterControl;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.List;

public class BasicQueryTest extends AbstractMemoriaTest {
  
  public void test_getAll_by_class_literal() {
    StringObject obj = new StringObject("1");
    save(obj);
    
    reopen();
    
    assertEquals(1, fObjectStore.query(StringObject.class).size());
  }
  
  public void test_getAll_by_class_name() {
    StringObject obj = new StringObject("1");
    save(obj);
    
    reopen();
    
    assertEquals(1, fObjectStore.query(StringObject.class.getName()).size());
  }
  
  public void test_getAll_class_name_with_filter() {
    StringObject obj = new StringObject("1");
    save(obj);
    
    reopen();
    
    List<Object> result = fObjectStore.query(Object.class, new IFilter<Object>() {

      @Override
      public boolean accept(Object object, IFilterControl control) {
        return false;
      }
    });
    
    assertEquals(0, result.size());
  }

}
