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

import org.memoriadb.handler.collection.ICollectionDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.test.testclasses.TestValueObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class CollectionTest extends AbstractMemoriaTest {
  
  public void test_collection_with_objectIds() {
    Collection<IObjectId> collection = createCollection();
    collection.add(save(new Object()));
    collection.add(save(new Object()));
    collection.add(save(new Object()));
    
    IObjectId collectionId = save(collection);
    
    reopen();
    
    Collection<IObjectId> l1_collection = fObjectStore.get(collectionId);
    assertEquals(collection, l1_collection);
  }
  
  public void test_collection_with_value_objects() {
    List<TestValueObject> list = new ArrayList<TestValueObject>();
    
    for(int i = 0; i < 3; ++i) {
      list.add(new TestValueObject(Integer.toString(i)));
    }
    
    IObjectId id = save(list);
    
    reopen();
    
    List<TestValueObject> l1_list = fObjectStore.get(id);
    assertEquals(list, l1_list);
  }

  public void test_data_mode() {
    Collection<StringObject> objectList = getObjectCollection();
    IObjectId objectId = saveAll(objectList);
    
    reopenDataMode();
    
    ICollectionDataObject l1_collection = fDataStore.get(objectId);
    assertEquals(objectList.size(), l1_collection.getCollection().size());
    l1_collection.getCollection().clear();
    save(l1_collection);
    
    reopen();
    
    Collection<Object> l2_collection = fObjectStore.get(objectId);
    assertEquals(l1_collection.getCollection(), l2_collection);
  }

  public void test_data_mode_scenario() {
    Collection<StringObject> objectList = getObjectCollection();
    IObjectId objectId = saveAll(objectList);
    
    reopenDataMode();
    
    ICollectionDataObject l1_collection = fDataStore.get(objectId);
    assertEquals(objectList.size(), l1_collection.getCollection().size());
    
    l1_collection.getCollection().clear();
    l1_collection.getCollection().add(StringObject.createFieldObject(fDataStore, "newObj"));
    
    saveAll(l1_collection);
    
    reopen();
    
    Collection<StringObject> l2_collection = fObjectStore.get(objectId);
    assertEquals(l1_collection.getCollection().size(), l2_collection.size());
    assertEquals("newObj", ((StringObject)getElement(0, l2_collection)).getString());
  }
  
  public void test_empty_collection() {
    Collection<Object> collection = createCollection();
    reopen(collection);
  }
  
  public void test_int_object() {
    Collection<Integer> collection = getIntCollection();
    reopen(collection);
  }
  
  public void test_list_in_list() {
    Collection<Collection<?>> collection = createCollection();
    collection.add(getIntCollection());
    collection.add(getShortCollection());
    collection.add(getObjectCollection());
    
    reopen(collection);
  }
  
  public void test_list_in_list_in_multiple_save_calls() {
    Collection<Collection<?>> collection = createCollection();
    collection.add(getIntCollection());
    collection.add(getShortCollection());
    collection.add(getObjectCollection());
    
    for(Collection<?> subCollection: collection) {
      saveAll(subCollection);
    }
    
    reopen(collection);
  }
  
  public void test_list_lifecycle() {
    Collection<StringObject> collection = getObjectCollection();
    StringObject obj1 = collection.iterator().next();
    reopen(collection);
    
    collection.clear();
    reopen(collection);
    
    collection.add(new StringObject("3"));
    reopen(collection);
    
    collection.add(obj1);
    reopen(collection);
  }
  
  public void test_mixed_list() {
    Collection<Object> collection = createCollection();
    collection.add("1");
    collection.add(getIntCollection());
    collection.add(new StringObject());
    
    reopen(collection);
  }

  public void test_null_reference() {
    Collection<StringObject> collection = createCollection();
    collection.add(new StringObject("1"));
    collection.add(null);
    collection.add(new StringObject("3"));
    
    IObjectId id = saveAll(collection);
    reopen();
    
    Collection<StringObject> l1_collection = get(id);
    assertEquals(3, l1_collection.size());
    
  }
  
  public void test_object() {
    Collection<StringObject> collection = getObjectCollection();
    reopen(collection);
  }
  
  public void test_save_all_data_mode() {
    reopenDataMode();
  }

  public void test_short_collection() {
    Collection<Short> collection = getShortCollection();
    assertEquals(Short.class, collection.iterator().next().getClass());
    
    reopen(collection);
  }
  
  public void test_string() {
    Collection<String> collection = createCollection();
    collection.add("one");
    collection.add("two");
    reopen(collection);
  }
  
  protected abstract <T> Collection<T> createCollection();
  
  protected Collection<Integer> getIntCollection() {
    Collection<Integer> collection = createCollection();
    collection.add(new Integer(1));
    collection.add(new Integer(2));
    return collection;
  }
  
  protected Collection<StringObject> getObjectCollection() {
    Collection<StringObject> collection = createCollection();
    collection.add(new StringObject("1"));
    collection.add(new StringObject("2"));
    return collection;
  }

  protected Collection<Short> getShortCollection() {
    Collection<Short> collection = createCollection();
    collection.add(Short.valueOf("200"));
    collection.add(Short.valueOf("210"));
    collection.add(Short.valueOf("211"));
    
    return collection;
  }
  
  protected void reopen(Collection<?> collection) {
    IObjectId id = saveAll(collection);
    reopen();
    assertEquals(collection, get(id));
  }
  
  private Object getElement(int index, Collection<?> collection) {
    Iterator<?> iterator = collection.iterator();
    for(int i = 0; i < index; ++i) {
      iterator.next();
    }
    return iterator.next();
  }
  
  private Collection<?> getStringCollection(int intEntryCount) {
    Collection<String> result = createCollection();
    for(int i = 0; i < intEntryCount; ++i) {
      result.add("A String " + i);
    }
    return result;
  }

}
