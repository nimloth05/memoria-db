package org.memoriadb.test.handler.map;

import java.util.*;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.SimpleTestObj;
import org.memoriadb.testutil.*;

public class LinkedHashMapOrderTest extends AbstractMemoriaTest {
 
  public void test_mixed_objects() {
    List<Object> keys = new ArrayList<Object>();
    keys.add("0");
    keys.add("10");
    keys.add("20");
    keys.add(new SimpleTestObj("30"));
    keys.add(new SimpleTestObj("40"));
    keys.add(50);
    keys.add(60);
    
    HashMap<Object, Object> map = new LinkedHashMap<Object, Object>();
    map.put(keys.get(0), 0);
    map.put(keys.get(1), 10);
    map.put(keys.get(2), new SimpleTestObj("20"));
    map.put(keys.get(3), new SimpleTestObj("30"));
    map.put(keys.get(4), 40);
    map.put(keys.get(5), 50);
    map.put(keys.get(6), null);
    
    IObjectId id = saveAll(map);
    
    reopen();
    
    HashMap<Object, Object> l1_map = get(id);
    
    // test content
    assertTrue(map.equals(l1_map));
    
    // test order
    CollectionUtil.assertIterable(keys, l1_map.keySet());
    
  }
}
