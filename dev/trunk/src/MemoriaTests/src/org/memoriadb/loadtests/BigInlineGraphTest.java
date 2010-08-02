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

package org.memoriadb.loadtests;

import org.memoriadb.CreateConfig;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * HashMaps are stored inline in an Array, many updates occur.
 * 
 * @author msc
 *
 */
public class BigInlineGraphTest extends AbstractMemoriaTest {

  private int fInactiveThreshold;
  private int fSizeThreshold;

  public void test() {
    for (int inactiveThreshold = 0; inactiveThreshold <= 100; inactiveThreshold += 17) {
      for (int sizeThreshold = 0; sizeThreshold <= 100; sizeThreshold += 29) {
        fInactiveThreshold = inactiveThreshold;
        fSizeThreshold = sizeThreshold;

        executeTest();

      }
    }
  }

  @Override
  protected void configureOpen(CreateConfig config) {
    configure(config);
  }

  @Override
  protected void configureReopen(CreateConfig config) {
    configure(config);
  }

  private void configure(CreateConfig config) {
    config.setBlockManager(new MaintenanceFreeBlockManager(fInactiveThreshold, fSizeThreshold));
    config.addValueClass(HashMap.class);
  }

  private HashMap<Integer, String> createHashMap() {
    HashMap<Integer, String> result = new HashMap<Integer, String>();
    
    result.put(1, "one");
    result.put(2, "two");
    return result;
  }

  private void executeTest() {
    List<Object> entities = new ArrayList<Object>();
    List<HashMap<Integer,String>[]> root = new ArrayList<HashMap<Integer,String>[]>();
    entities.add(root);
    
    for(int i = 0; i < 10; ++i) {
      root.add(getArray(entities));
    }
    
    IObjectId id = saveAll(root);
    
    for(int i=0; i < 20; ++i) {
      update(entities);
    }
    
    reopen();
    
    root = get(id);
    
    assertEquals("two", root.get(0)[0].get(2));
    
  }

  @SuppressWarnings("unchecked")
  private HashMap<Integer, String>[] getArray(List<Object> entities) {
    HashMap<Integer, String>[] result = new HashMap[5];
    entities.add(result);
    for(int i = 0; i < 5; ++i) {
      // HashMaps are not added to the entity-list, because the are value-object and can not be updated.
      result[i] = createHashMap();
    }
    return result;
  }

  private void update(List<Object> entities) {
    for(Object obj: entities){
      save(obj);
    }
  }

}
