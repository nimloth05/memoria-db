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

package org.memoriadb.test.block;

import org.memoriadb.block.Block;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.block.IBlockManagerExt;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.HashSet;

public class MaintenanceFreeBlockManagerTest extends AbstractMemoriaTest {
  
  /**
   * One inactive object is enough to make ready for recycling
   */
  public void test_inactiveThreshold_0() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(0, 0);

    Block b10 = new Block(10, 0);
    b10.addObjectIds(createObjectIdSet(20));
    manager.add(b10);
    
    assertNull(manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
    
    // now one ObjectData becomes inactive
    b10.incrementInactiveObjectDataCount();

    assertNull(manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
    assertNotNull(manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
  }

  public void test_inactiveThreshold_50_scenario() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(50, 0);

    Block b10a = new Block(10, 0);
    b10a.addObjectIds(createObjectIdSet(2));
    manager.add(b10a);
    
    assertNull(manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
    
    b10a.incrementInactiveObjectDataCount();
    assertNull(manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
    assertSame(b10a, manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
    
    Block b10b = new Block(10, 1);
    b10b.addObjectIds(createObjectIdSet(2));
    manager.add(b10b);

    assertNull(manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
    
    b10b.incrementInactiveObjectDataCount();
    assertNull(manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
    assertSame(b10b, manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
  }
  
  public void test_incativeThreshold_50() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(50, 0);

    Block b10a = new Block(10, 0);
    b10a.addObjectIds(createObjectIdSet(2));
    manager.add(b10a);

    assertNull(manager.allocatedRecyclebleBlock(20, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));

    b10a.incrementInactiveObjectDataCount();

    assertNull(manager.allocatedRecyclebleBlock(20, new HashSet<Block>()));
    assertSame(b10a, manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));

    // block is gone...
    assertNull(manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
  }
  
  /**
   * Two blocks with the same size are added. The set block-Manager uses 50/50 for inactive- and size-Threshold.
   */
  public void test_incativeThreshold_50_two_same_sizes() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(50, 0);

    Block b10a = new Block(10, 0);
    b10a.addObjectIds(createObjectIdSet(2));

    Block b10b = new Block(10, 1);
    b10b.addObjectIds(createObjectIdSet(2));

    manager.add(b10b);
    manager.add(b10a);

    assertEquals(2, manager.getBlockCount());
    assertEquals(0, manager.getRecyclingBlockCount());

    // b has inactiveRatio: 1 / 2
    b10a.incrementInactiveObjectDataCount();
    b10b.incrementInactiveObjectDataCount();

    assertEquals(2, manager.getBlockCount());
    assertEquals(2, manager.getRecyclingBlockCount());

    assertNotNull(manager.allocatedRecyclebleBlock(1, new HashSet<Block>()));
    assertNotNull(manager.allocatedRecyclebleBlock(1, new HashSet<Block>()));

    // no more blocks for recycling
    assertNull(manager.allocatedRecyclebleBlock(1, new HashSet<Block>()));
  }

  public void test_same_Block_is_not_added_twice() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(21, 0);
    
    Block block = new Block(10, 0);
    block.setBlockManager(manager);
    
    block.addObjectIds(createObjectIdSet(5));
    
    assertEquals(0, manager.getRecyclingBlockCount());
    
    block.incrementInactiveObjectDataCount();
    assertEquals(0, manager.getRecyclingBlockCount());

    // 21% inactiveThreshold-mark exceeded.
    block.incrementInactiveObjectDataCount();
    assertEquals(1, manager.getRecyclingBlockCount());

    // still one, the same block can not be added twice!
    block.incrementInactiveObjectDataCount();
    assertEquals(1, manager.getRecyclingBlockCount());
    
    // remove the block
    Block found = manager.allocatedRecyclebleBlock(1, new HashSet<Block>());
    assertSame(block, found);
    
    assertEquals(0, manager.getRecyclingBlockCount());
    
  }

  /**
   * The size doesn't matter
   */
  public void test_sizeThreshold_0() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(0, 0);

    Block b = new Block(1000, 0);
    b.addObjectIds(createObjectIdSet(20));
    manager.add(b);
    
    b.incrementInactiveObjectDataCount();
    
    assertNull(manager.allocatedRecyclebleBlock(1001, new HashSet<Block>()));
    assertNotNull(manager.allocatedRecyclebleBlock(1, new HashSet<Block>()));
  }
  
  /**
   * The block mustn't be bigger than twice the requested size.
   */
  public void test_sizeThreshold_100() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(0, 100);

    Block b = new Block(1000, 0);
    b.addObjectIds(createObjectIdSet(20));
    manager.add(b);
    
    b.incrementInactiveObjectDataCount();
    
    assertNull(manager.allocatedRecyclebleBlock(1001, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(999, new HashSet<Block>()));
    assertNotNull(manager.allocatedRecyclebleBlock(1000, new HashSet<Block>()));
  }
  
  /**
   * The block mustn't be bigger than twice the requested size.
   */
  public void test_sizeThreshold_50() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(0, 50);

    Block b = new Block(1000, 0);
    b.addObjectIds(createObjectIdSet(20));
    manager.add(b);
    
    b.incrementInactiveObjectDataCount();
    
    assertNull(manager.allocatedRecyclebleBlock(1001, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(1, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(499, new HashSet<Block>()));
    assertNotNull(manager.allocatedRecyclebleBlock(500, new HashSet<Block>()));
  }
  
  public void test_tabooBlocks_are_not_returned() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(0, 0);

    Block b = new Block(1000, 0);
    b.addObjectIds(createObjectIdSet(10));
    b.incrementInactiveObjectDataCount();
    
    manager.add(b);

    HashSet<Block> hashSet = new HashSet<Block>();
    hashSet.add(b);
    assertNull(manager.allocatedRecyclebleBlock(10, hashSet));
  }

}
