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
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager2;
import org.memoriadb.core.block.IBlockManagerExt;
import org.memoriadb.core.file.FileLayout;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.HashSet;

/**
 * @author Sandro
 */
public final class MaintenanceFreeBlockManager2Test extends AbstractMemoriaTest {

  public void test_save() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager2(0);

    Block block = new Block(10, 0);
    block.addObjectIds(createObjectIdSet(10));
    manager.add(block);

    block.incrementInactiveObjectDataCount();

    assertNotNull("block not found", manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
    assertNull("block is already used, so no recyclable blocks are available", manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
  }

  public void test_allocatedRecyclebleBlock_with_no_blocks_ready() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager2(0);

    Block block = new Block(10, 0);
    block.addObjectIds(createObjectIdSet(20));
    manager.add(block);

    assertNull(manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
  }

  public void test_allocateRecyclableBlock_with_no_exact_math_in_size() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager2(50);

    Block block = new Block(10, 0);
    block.addObjectIds(createObjectIdSet(2));
    manager.add(block);

    block.incrementInactiveObjectDataCount();

    assertSame(block, manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
  }

  public void test_inactiveThreshold_50_scenario() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager2(50);

    Block b10a = new Block(10, 0);
    b10a.addObjectIds(createObjectIdSet(2));
    manager.add(b10a);

    b10a.incrementInactiveObjectDataCount();
    assertSame(b10a, manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));

    Block b10b = new Block(10, 1);
    b10b.addObjectIds(createObjectIdSet(2));
    manager.add(b10b);

    b10b.incrementInactiveObjectDataCount();
    assertSame(b10b, manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
    assertNull(manager.allocatedRecyclebleBlock(11, new HashSet<Block>()));
  }

    public void test_incativeThreshold_50_request_blocksize_is_bigger_then_recyclable_block() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager2(50);

    Block block = new Block(10, 0);
    block.addObjectIds(createObjectIdSet(2));
    manager.add(block);

    block.incrementInactiveObjectDataCount();

    assertNull(manager.allocatedRecyclebleBlock(FileLayout.BLOCK_OVERHEAD + 20, new HashSet<Block>()));
    assertSame(block, manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
    //block is used now.
    assertNull(manager.allocatedRecyclebleBlock(10, new HashSet<Block>()));
  }


}
