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
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.test.testclasses.inheritance.A;
import org.memoriadb.test.testclasses.inheritance.B;
import org.memoriadb.test.testclasses.inheritance.C;
import org.memoriadb.testutil.AbstractMemoriaTest;
import org.memoriadb.testutil.CollectionUtil;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author sandro
 *
 */
public class ObjectModeQueryTest extends AbstractMemoriaTest {
  
  public void test_getAll_class_literal_with_filter() {
    StringObject obj = new StringObject("1");
    save(obj);
    
    reopen();
    
    List<StringObject> result = fObjectStore.query(StringObject.class, new IFilter<StringObject>() {

      @Override
      public boolean accept(StringObject object, IFilterControl control) {
        return false;
      }
    });
    
    assertEquals(0, result.size());
  }
  
  public void test_getAllUserSpaceObjects() {
    assertEquals(0, CollectionUtil.count(fObjectStore.getAllUserSpaceObjects()));
  }
  
  public void test_memoria_class() {
    List<IMemoriaClass> query = fObjectStore.query(IMemoriaClass.class);
    assertEquals(0, query.size());
  }
  
  public void test_polymorph_query() {
    StringObject obj = new StringObject("1");
    save(obj);
    
    reopen();
    
    List<Object> result = fObjectStore.query(Object.class);
    assertEquals(CollectionUtil.count(fObjectStore.getAllUserSpaceObjects()), result.size());
  }
  
  public void test_polymorph_query_with_field_objects() {
    fObjectStore.beginUpdate();
    
    save(new A());
    save(new B());
    save(new C());
    
    fObjectStore.endUpdate();
        
  }
  
  @SuppressWarnings("unchecked")
  public void test_query_abstract_classes() {
    fObjectStore.save(new ArrayList<Object>());
    List<AbstractList> query = fObjectStore.query(AbstractList.class);
    assertEquals(1, query.size());
  }
  
}
