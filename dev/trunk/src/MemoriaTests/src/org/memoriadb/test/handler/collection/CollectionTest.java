package org.memoriadb.test.handler.collection;

import java.util.*;

import org.memoriadb.handler.collection.ICollectionDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.*;
import org.memoriadb.testutil.AbstractMemoriaTest;

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
    Collection<SimpleTestObj> objectList = getObjectCollection();
    IObjectId objectId = saveAll(objectList);
    
    reopenDataMode();
    
    ICollectionDataObject l1_collection = fDataStore.get(objectId);
    assertEquals(objectList.size(), l1_collection.getCollection().size());
    l1_collection.getCollection().clear();
    save(l1_collection);
    
    reopen();
    
    Collection<SimpleTestObj> l2_collection = fObjectStore.get(objectId);
    assertEquals(l1_collection.getCollection(), l2_collection);
  }

  public void test_data_mode_scenario() {
    Collection<SimpleTestObj> objectList = getObjectCollection();
    IObjectId objectId = saveAll(objectList);
    
    reopenDataMode();
    
    ICollectionDataObject l1_collection = fDataStore.get(objectId);
    assertEquals(objectList.size(), l1_collection.getCollection().size());
    
    l1_collection.getCollection().clear();
    l1_collection.getCollection().add(SimpleTestObj.createFieldObject(fDataStore, "newObj"));
    
    saveAll(l1_collection);
    
    reopen();
    
    Collection<SimpleTestObj> l2_collection = fObjectStore.get(objectId);
    assertEquals(l1_collection.getCollection().size(), l2_collection.size());
    assertEquals("newObj", ((SimpleTestObj)getElement(0, l2_collection)).getString());
  }
  
  public void test_empty_collection() {
    Collection<Object> collection = createCollection();
    reopen(collection);
  }
  
  public void test_int_object() {
    Collection<Integer> collection = getIntObjectCollection();
    reopen(collection);
  }
  
  public void test_int_primitive() {
    Collection<Integer> list = getIntPrimitiveCollection();
    reopen(list);
  }
  
  public void test_list_in_list() {
    Collection<Collection<?>> collection = createCollection();
    collection.add(getIntObjectCollection());
    collection.add(getIntPrimitiveCollection());
    collection.add(getObjectCollection());
    
    reopen(collection);
  }
  
  public void test_list_lifecycle() {
    Collection<SimpleTestObj> collection = getObjectCollection();
    SimpleTestObj obj1 = collection.iterator().next();
    reopen(collection);
    
    collection.clear();
    reopen(collection);
    
    collection.add(new SimpleTestObj("3"));
    reopen(collection);
    
    collection.add(obj1);
    reopen(collection);
  }
  
  public void test_mixed_list() {
    Collection<Object> collection = createCollection();
    collection.add("1");
    collection.add(getIntObjectCollection());
    collection.add(new SimpleTestObj());
    
    reopen(collection);
  }
  
  public void test_null_reference() {
    Collection<SimpleTestObj> collection = createCollection();
    collection.add(new SimpleTestObj("1"));
    collection.add(null);
    collection.add(new SimpleTestObj("3"));
    
    IObjectId id = saveAll(collection);
    reopen();
    
    Collection<SimpleTestObj> l1_collection = get(id);
    assertEquals(3, l1_collection.size());
    
  }

  public void test_object() {
    Collection<SimpleTestObj> collection = getObjectCollection();
    reopen(collection);
  }
  
  public void test_save_all_data_mode() {
    reopenDataMode();
    
    
  }
  
  public void test_string() {
    Collection<String> collection = createCollection();
    collection.add("one");
    collection.add("two");
    reopen(collection);
  }
  
  protected abstract <T> Collection<T> createCollection();
  
  protected Collection<Integer> getIntObjectCollection() {
    Collection<Integer> collection = createCollection();
    collection.add(new Integer(1));
    collection.add(new Integer(2));
    return collection;
  }

  protected Collection<Integer> getIntPrimitiveCollection() {
    Collection<Integer> collection = createCollection();
    collection.add(1);
    collection.add(1);
    return collection;
  }
  
  protected Collection<SimpleTestObj> getObjectCollection() {
    Collection<SimpleTestObj> collection = createCollection();
    collection.add(new SimpleTestObj("1"));
    collection.add(new SimpleTestObj("2"));
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
  
}
