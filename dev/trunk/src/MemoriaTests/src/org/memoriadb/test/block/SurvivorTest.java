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
import org.memoriadb.block.Block;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

/**
 * Tests the reuse of blocks with a MaintenanceFreeBlockManager.
 * 
 * @author msc
 * 
 */
public class SurvivorTest extends AbstractMemoriaTest {

  public void scenarioTest() {
    Object o1 = new Object();
    
    beginUpdate();
    save(o1);
    save(o1);
    save(o1);
    endUpdate();
    // |o1,o2,o3|
    
    delete(o1);
    // |o1,o2,o3|
  }

  public void test_current_block_from_updatee_is_not_recycled() {
    beginUpdate();
    IObjectId id1 = save(new Object());
    IObjectId id2 = save(new Object());
    endUpdate();

    delete(get(id1));

    assertEquals(3, getBlockManager().getBlockCount());

    // update o2 from block0. block0 must not be recycled, because o2 resides in block0
    save(get(id2));

    assertEquals(4, getBlockManager().getBlockCount());

    assertEquals(2, getBlock(1).getInactiveObjectDataCount());
    assertEquals(getBlock(2), getCurrentBlock(id1));
    assertEquals(getBlock(3), getCurrentBlock(id2));
  }

  public void test_deletionMarker_is_survivor() {
    Object o1 = new Object();
    IObjectId id1 = save(o1);
    // |o1|

    beginUpdate();
    Object o2 = new Object();
    IObjectId id2 = save(o2);
    delete(o1);
    endUpdate();
    // |~o1|o2,d1|

    save(o2);
    // |o2'|~o2,d1|

    save(o2);
    // |~o2|o2''|

    reopen();

    assertFalse(fObjectStore.containsId(id1));
    assertTrue(fObjectStore.containsId(id2));
  }
  
  public void test_inactiveCount() {
    Object obj = new Object();

    save(obj);
    assertEquals(0, getBlock(1).getInactiveRatio());

    save(obj);
    assertEquals(100, getBlock(1).getInactiveRatio());
    assertEquals(0, getBlock(2).getInactiveRatio());

    save(obj);
    assertEquals(0, getBlock(1).getInactiveRatio());
    assertEquals(100, getBlock(2).getInactiveRatio());

    reopen();
    assertEquals(0, getBlock(1).getInactiveRatio());
    assertEquals(100, getBlock(2).getInactiveRatio());
  }

  public void test_inactiveRatio_when_a_block_is_reused() {
    Object obj = new Object();
    save(obj);
    assertEquals(2, getBlockManager().getBlockCount());
    assertEquals(0, getBlock(0).getInactiveRatio());

    delete(obj);
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(100, getBlock(1).getInactiveRatio());

    save(new Object());
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getBlock(1).getInactiveRatio());
  }

  public void test_obsolete_deletion_marker() {
    IObjectId id1 = save(new Object());
    // |o1|

    // second block, block 0 has one inactive, one active.
    delete(get(id1));
    // |~o1|d1|

    IObjectId id2 = save(new Object());
    // |o2|d1|
    
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(0, getBlock(1).getInactiveRatio());
    assertEquals(100, getBlock(2).getInactiveRatio());
    
    reopen();
    
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(0, getBlock(1).getInactiveRatio());
    assertEquals(100, getBlock(2).getInactiveRatio());
  }

  public void test_oldGenerationCount_for_survivor() {
    Object o1 = new Object();
    Object o2 = new Object();

    beginUpdate();
    IObjectId id1 = save(o1);
    IObjectId id2 = save(o2);
    endUpdate();
    // |o1,o2|

    assertEquals(2, getBlockManager().getBlockCount());
    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());

    save(o1);
    // |~o1,o2|o1'|

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(1, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());

    IObjectId id3 = save(new Object());
    // |o3|o1'|o2'|

    assertEquals(4, getBlockManager().getBlockCount());
    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id3).getOldGenerationCount());
  }

  public void test_reusing_one_block() {
    Object o1 = new Object();
    IObjectId id1 = save(o1);
    assertEquals(2, getBlockManager().getBlockCount());

    reopen();

    // deleting the object, what results in a free block and a deletionMarker.
    save(get(id1));

    // let o2 reuse the free block
    IObjectId id2 = save(new Object());

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(getBlock(1), getCurrentBlock(id2));
    assertEquals(getBlock(2), getCurrentBlock(id1));
    assertFalse(getObjectInfo(id2).isDeleted());
    assertTrue(fObjectStore.containsId(id1));
    assertTrue(fObjectStore.containsId(id2));

    reopen();

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(getBlock(1), getCurrentBlock(id2));
    assertEquals(getBlock(2), getCurrentBlock(id1));
    assertFalse(getObjectInfo(id2).isDeleted());
    assertTrue(fObjectStore.containsId(id1));
    assertTrue(fObjectStore.containsId(id2));
  }

  public void test_safing_survivor() {

    beginUpdate();
    IObjectId id1 = save(new Object());
    IObjectId id2 = save(new Object());
    endUpdate();
    // |o1,o2|

    // second block, block 0 has one inactive, one active.
    delete(get(id1));
    // |~o1,o2|d1|

    assertEquals(3, getObjectInfo(id1).getRevision());
    assertEquals(1, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(2, getObjectInfo(id2).getRevision());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());

    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(50, getBlock(1).getInactiveRatio());
    assertEquals(0, getBlock(2).getInactiveRatio());

    // block2 must not be overwritten, because block2 contains a deleteMarker which is not obsolete during the operation 
    IObjectId id3 = save(new Object());
    // |o3|d1|o2'|

    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertTrue(getObjectInfo(id1).isDeleted());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertFalse(getObjectInfo(id2).isDeleted());
    assertEquals(0, getObjectInfo(id3).getOldGenerationCount());
    assertFalse(getObjectInfo(id3).isDeleted());

    assertEquals(4, getBlockManager().getBlockCount());
    assertEquals(0, getBlock(1).getInactiveRatio());
    assertEquals(100, getBlock(2).getInactiveRatio());
    assertEquals(0, getBlock(3).getInactiveRatio());

    assertEquals(3, getObjectInfo(id1).getRevision());
    assertEquals(5, getObjectInfo(id2).getRevision());
    assertEquals(4, getObjectInfo(id3).getRevision());

    assertFalse(fObjectStore.containsId(id1));
    assertTrue(fObjectStore.containsId(id2));
    assertTrue(fObjectStore.containsId(id3));

    assertEquals(getBlock(2), getCurrentBlock(id1));
    assertEquals(getBlock(3), getCurrentBlock(id2));
    assertEquals(getBlock(1), getCurrentBlock(id3));

    reopen();

    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertTrue(getObjectInfo(id1).isDeleted());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertFalse(getObjectInfo(id2).isDeleted());
    assertEquals(0, getObjectInfo(id3).getOldGenerationCount());
    assertFalse(getObjectInfo(id3).isDeleted());

    assertEquals(4, getBlockManager().getBlockCount());
    assertEquals(0, getBlock(1).getInactiveRatio());
    assertEquals(100, getBlock(2).getInactiveRatio());
    assertEquals(0, getBlock(3).getInactiveRatio());

    assertEquals(3, getObjectInfo(id1).getRevision());
    assertEquals(5, getObjectInfo(id2).getRevision());
    assertEquals(4, getObjectInfo(id3).getRevision());

    assertFalse(fObjectStore.containsId(id1));
    assertTrue(fObjectStore.containsId(id2));
    assertTrue(fObjectStore.containsId(id3));

    assertEquals(getBlock(2), getCurrentBlock(id1));
    assertEquals(getBlock(3), getCurrentBlock(id2));
    assertEquals(getBlock(1), getCurrentBlock(id3));
    
    save(get(id3));
    // |o3~|o3'|o2'|
    
    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(1, getObjectInfo(id3).getOldGenerationCount());

    assertEquals(4, getBlockManager().getBlockCount());
    assertEquals(100, getBlock(1).getInactiveRatio());
    assertEquals(0, getBlock(2).getInactiveRatio());
    assertEquals(0, getBlock(3).getInactiveRatio());
    assertEquals(getBlock(3), getCurrentBlock(id2));
    assertEquals(getBlock(2), getCurrentBlock(id3));
    
    reopen();
    
    assertNull(getObjectInfo(id1));
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(1, getObjectInfo(id3).getOldGenerationCount());
    
    assertEquals(4, getBlockManager().getBlockCount());
    assertEquals(100, getBlock(1).getInactiveRatio());
    assertEquals(0, getBlock(2).getInactiveRatio());
    assertEquals(0, getBlock(3).getInactiveRatio());
    assertEquals(getBlock(3), getCurrentBlock(id2));
    assertEquals(getBlock(2), getCurrentBlock(id3));
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

  private Block getCurrentBlock(IObjectId id) {
    return getObjectInfo(id).getCurrentBlock();
  }

}
