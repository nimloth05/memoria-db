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

/**
 * Test that obsolet delete-markers are removed!
 * 
 * @author msc
 */
public class DeleteMarkerTest extends AbstractMemoriaTest {

  public void test_deleteMarker_is_removed() {
    Object o1 = new Object();
    Object o2 = new Object();
    Object o3 = new Object();

    save(o1);
    beginUpdate();
    delete(o1);
    IObjectId id2 = save(o2);
    endUpdate();
    // |~o1|d1,o2|
    o1 = null;
    o2 = null;

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(100, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(0, getBlockManager().getBlock(2).getInactiveRatio());
    reopen();
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(100, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(0, getBlockManager().getBlock(2).getInactiveRatio());

    save(get(id2));
    // |o2'|d1,~o2|

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(100, getBlockManager().getBlock(2).getInactiveRatio());
    reopen();
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(100, getBlockManager().getBlock(2).getInactiveRatio());

    save(get(id2));
    // |~o2'|o2''|

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(100, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(0, getBlockManager().getBlock(2).getInactiveRatio());
    assertEquals(1, getObjectInfo(id2).getOldGenerationCount());
    assertBlocks(getBlock(2), getObjectInfo(id2).getCurrentBlock());
    reopen();
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(100, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(0, getBlockManager().getBlock(2).getInactiveRatio());
    assertEquals(1, getObjectInfo(id2).getOldGenerationCount());
    assertBlocks(getBlock(2), getObjectInfo(id2).getCurrentBlock());

    delete(get(id2));
    // |d2|~o2''|

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(100, getBlockManager().getBlock(2).getInactiveRatio());
    assertFalse(fObjectStore.containsId(id2));
    reopen();
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(100, getBlockManager().getBlock(2).getInactiveRatio());
    assertFalse(fObjectStore.containsId(id2));

    IObjectId id3 = save(o3);
    // |d2|o3|
    o3 = null;

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(100, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(0, getBlockManager().getBlock(2).getInactiveRatio());
    assertBlocks(getBlock(1), getObjectInfo(id2).getCurrentBlock());
    assertBlocks(getBlock(2), getObjectInfo(id3).getCurrentBlock());
    assertTrue(getObjectInfo(id2).isDeleted());
    assertFalse(getObjectInfo(id3).isDeleted());
    assertTrue(getObjectInfo(id2).isDeleteMarkerPersistent());
    assertFalse(getObjectInfo(id3).isDeleteMarkerPersistent());
    reopen();
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(100, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(0, getBlockManager().getBlock(2).getInactiveRatio());
    assertBlocks(getBlock(1), getObjectInfo(id2).getCurrentBlock());
    assertBlocks(getBlock(2), getObjectInfo(id3).getCurrentBlock());
    assertTrue(getObjectInfo(id2).isDeleteMarkerPersistent());
    assertFalse(getObjectInfo(id3).isDeleteMarkerPersistent());
    assertTrue(getObjectInfo(id2).isDeleteMarkerPersistent());
    assertFalse(getObjectInfo(id3).isDeleteMarkerPersistent());

    save(get(id3));
    // |o3'|~o3|

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(100, getBlockManager().getBlock(2).getInactiveRatio());
    assertBlocks(getBlock(1), getObjectInfo(id3).getCurrentBlock());
    assertEquals(1, getObjectInfo(id3).getOldGenerationCount());
    reopen();
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(100, getBlockManager().getBlock(2).getInactiveRatio());
    assertEquals(1, getObjectInfo(id3).getOldGenerationCount());
  }

  public void test_DeleteMarker_is_survivor() {
    Object o1 = new Object();
    Object o2 = new Object();
    Object o3 = new Object();

    beginUpdate();
    save(o1);
    save(o2);
    IObjectId id3 =save(o3);
    endUpdate();

    Object o4 = new Object();
    Object o5 = new Object();

    beginUpdate();
    delete(o3);
    save(o4);
    save(o5);
    endUpdate();

    beginUpdate();
    delete(o4);
    delete(o5);
    endUpdate();
    // |o1,o2,~o3|d3,~o4,~o5|d4,d5|

    assertEquals(4, getBlockManager().getBlockCount());
    assertEquals(33, getBlock(1).getInactiveRatio());
    assertEquals(66, getBlock(2).getInactiveRatio());
    assertEquals(0, getBlock(3).getInactiveRatio());

    Object o6 = new Object();
    save(o6);
    // |o1,o2,~o3|o6|~d4,~d5|d3|
    
    assertTrue(getObjectInfo(id3).isDeleted());
    assertEquals(5, getBlockManager().getBlockCount());
    assertEquals(33, getBlock(1).getInactiveRatio());
    assertEquals(0, getBlock(2).getInactiveRatio());
    assertEquals(100, getBlock(3).getInactiveRatio()); // two obsolete delete-markers
    assertEquals(0, getBlock(4).getInactiveRatio());
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
    config.setBlockManager(new MaintenanceFreeBlockManager(50, 0));
    config.setUseCompression(false);
  }

}