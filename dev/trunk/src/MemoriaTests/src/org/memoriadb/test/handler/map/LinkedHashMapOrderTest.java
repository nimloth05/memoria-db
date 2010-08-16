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

package org.memoriadb.test.handler.map;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;
import org.memoriadb.testutil.CollectionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class LinkedHashMapOrderTest extends AbstractMemoriaTest {
 
  public void test_mixed_objects() {
    List<Object> keys = new ArrayList<Object>();
    keys.add("0");
    keys.add("10");
    keys.add("20");
    keys.add(new StringObject("30"));
    keys.add(new StringObject("40"));
    keys.add(50);
    keys.add(60);
    
    HashMap<Object, Object> map = new LinkedHashMap<Object, Object>();
    map.put(keys.get(0), 0);
    map.put(keys.get(1), 10);
    map.put(keys.get(2), new StringObject("20"));
    map.put(keys.get(3), new StringObject("30"));
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
