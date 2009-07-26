package org.memoriadb.test.handler.map;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.ObjectReferencer;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.test.testclasses.TestValueObject;
import org.memoriadb.testutil.AbstractMemoriaTest;
import org.memoriadb.testutil.CollectionUtil;

import java.util.Map;

public abstract class AbstractMapTest extends AbstractMemoriaTest {
  
  public void test_aggregated_map() {
    ObjectReferencer obj = new ObjectReferencer();
    obj.setObject(createMap());
    
    IObjectId id = saveAll(obj);
    
    reopen();
    
    obj = get(id);
    assertTrue(obj.getObject() instanceof Map);
  }
  
  public void test_map_containing_array() {
    StringObject[] arr = new StringObject[]{new StringObject("1")};
    Map<Integer, StringObject[]> map = createMap();
    map.put(1, arr);
    
    saveAll(arr);
    IObjectId id = save(map);
    
    reopen();
    
    map = get(id);
    
    assertEquals(new StringObject("1"), map.get(1)[0]);
  }
  
  public void test_map_containing_itself() {
    Map<Integer, Map<?,?>> map = createMap();
    map.put(1, map);
    IObjectId id = saveAll(map);
    
    Map<Integer, Map<?,?>> l1_map = get(id);
    
    assertEquals(1, l1_map.size());
    assertSame(l1_map, l1_map.get(1));
  }
  
  @SuppressWarnings("unchecked")
  public void test_map_containing_map_with_back_ref() {
    Map<Object, Object> map1  = createMap();
    Map<Object, Object> map2 = createMap();
    
    Integer key1 = 1;
    Integer key2 = 2;
    
    map1.put(key1, map2);
    map2.put(key2, map1);
    
    IObjectId id = saveAll(map1);
    
    reopen();
    
    Map<Integer, Map<?,?>> l1_map1 = get(id);
    
    Map<Object, Object> l1_map2 = (Map<Object, Object>)l1_map1.get(key1);
    assertNotNull(l1_map2);
    assertSame(l1_map1, l1_map2.get(key2));
  }
  
  public void test_mixed_map() {
    Map<Object, Object> map = createMap();
    map.put(1, map);
    map.put("1", 1);
    map.put(2, new StringObject("2"));
    map.put(new StringObject("key"), new StringObject("value"));
    
    IObjectId id = saveAll(map);
    
    Map<Object, Object> l1_map = get(id);
    
    assertEquals(4, l1_map.size());
    assertSame(l1_map, l1_map.get(1));
    assertEquals(1, l1_map.get("1"));
    assertEquals(new StringObject("2"), l1_map.get(2));
    map.put(new StringObject("value"), l1_map.get(new StringObject("key")));
  }
  
  public void test_null_value() {
    Map<Integer, String> map = createMap();
    map.put(1, null);
    IObjectId id = save(map);
    
    reopen();
    
    Map<Integer, String> l1_map = get(id);
    assertEquals(1, l1_map.size());
    assertNull(l1_map.get(1));
  }
  
  public void test_objects() {
    Map<Integer, StringObject> map = createMap();
    map.put(1, new StringObject("one"));
    map.put(2, new StringObject("two"));
    IObjectId id = saveAll(map);
    
    reopen();
    
    Map<Integer, String> l1_map = get(id);
    assertEquals(2, l1_map.size());
    assertEquals(new StringObject("one"), l1_map.get(1));
    assertEquals(new StringObject("two"), l1_map.get(2));
  }
  
  public void test_primitives() {
    Map<Integer, String> map = createMap();
    map.put(1, "1");
    map.put(2, "2");
    map.put(3, "3");
    
    IObjectId id = save(map);
    
    reopen();
    
    Map<Integer, String> l1_map = get(id);
    assertEquals(3, l1_map.size());
    assertEquals("1", l1_map.get(1));
    assertEquals("2", l1_map.get(2));
    assertEquals("3", l1_map.get(3));
  }
  
  public void test_updating_object_map() {
    Map<Integer, StringObject> map = createMap();
    map.put(1, new StringObject("1"));
    IObjectId id = saveAll(map);
    
    reopen();
    
    map = get(id);
    assertEquals(new StringObject("1"), map.put(1, new StringObject("2")));
    map.put(3, new StringObject("3"));
    saveAll(map);
    
    reopen();
    
    map = get(id);
    
    assertEquals(new StringObject("2"), map.get(1));
    assertEquals(new StringObject("3"), map.get(3));
  }
  
  public void test_updating_primitive_map() {
    Map<Integer, String> map = createMap();
    map.put(1, "1");
    IObjectId id = save(map);
    
    reopen();
    
    map = get(id);
    assertEquals("1", map.put(1, "2"));
    save(map);
    
    reopen();
    
    map = get(id);
    
    assertEquals("2", map.get(1));
  }
  
  public void test_valueObject_are_diffrent_instance_after_reopen() {
    Map<String, TestValueObject> map1 = createMap();
    Map<String, TestValueObject> map2 = createMap();
    
    TestValueObject v1 = new TestValueObject("1");
    
    map1.put("1", v1);
    map2.put("1", v1);
    
    IObjectId id1 = save(map1);
    IObjectId id2 = save(map2);
    assertSame(map1.get("1"), map2.get("1"));
    reopen();
    
    Map<String, TestValueObject> l1_map1 = get(id1);
    Map<String, TestValueObject> l1_map2 = get(id2);
    
    TestValueObject l1_map1_value = l1_map1.values().iterator().next();
    TestValueObject l1_map2_value = l1_map2.values().iterator().next();
    
    assertNotSame(l1_map1_value, l1_map2_value);
  }
  
  public void test_valueObject_as_key() {
    Map<TestValueObject, Object> map = createMap();
    
    TestValueObject k1 = new TestValueObject("1");
    TestValueObject k2 = new TestValueObject("2");
    
    map.put(k1, "1");
    map.put(k2, "2");
    
    IObjectId id = saveAll(map);
    
    reopen();
    
    Map<TestValueObject, Object> l1_map = fObjectStore.get(id);
    assertEquals(2, l1_map.size());
    CollectionUtil.containsAll(l1_map.keySet(), k1, k2);
  }
  
  public void test_valueObject_as_value() {
    Map<String, TestValueObject> map = createMap();
    
    TestValueObject v1 = new TestValueObject("1");
    TestValueObject v2 = new TestValueObject("2");
    
    map.put("1", v1);
    map.put("2", v2);
    
    IObjectId id = save(map);
    reopen();
    
    Map<String, TestValueObject> l1_map = get(id);
    
    assertEquals(map.size(), l1_map.size());    
    CollectionUtil.containsAll(l1_map.values(), v1, v2);
  }

  
  protected abstract <K, V> Map<K,V> createMap();
  
}
