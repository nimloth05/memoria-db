package org.memoriadb.test.core.handler.collection;

import java.util.Collection;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public abstract class CollectionTest extends AbstractObjectStoreTest {
  
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

  private void reopen(Collection<?> collection) {
    IObjectId id = saveAll(collection);
    reopen();
    assertEquals(collection, get(id));
  }


}
