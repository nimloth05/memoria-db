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

package org.memoriadb.test.block;

import org.memoriadb.CreateConfig;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class TestInactiveObjectDataCount extends AbstractMemoriaTest {

  public void test() {
    Object o1 = new Object();
    Object o2 = new Object();
    Object o3 = new Object();
    
    beginUpdate();
      IObjectId id1 = save(o1);
      IObjectId id2 = save(o2);
    endUpdate();
    
    beginUpdate();
      delete(o1);
      IObjectId id3 = save(o3);
    endUpdate();
    // |~o1, o2|d1, o3|
    
    assertEquals(1, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id3).getOldGenerationCount());
    
    save(o3);
    // |o3'|d1, ~o3|o2'|
    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(1, getObjectInfo(id3).getOldGenerationCount());
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
    config.setBlockManager(new MaintenanceFreeBlockManager(0, 0));
  }
}
