package org.memoriadb.test.core.block;

import org.memoriadb.core.block.Block;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.testutil.*;

public class BlockManagerTest extends AbstractObjectStoreTest {
  
  /**
   * Test that the Blocks are created properly, 
   * no matter if the file is initially read or the Block is appended while running.
   */
  public void test_blocks_are_created() {
    save(new Object());
    
    Block block_after_append = getBlockManager().getBlock(0);
    
    reopen();
    
    Block block_after_reopen = getBlockManager().getBlock(0);
    
    FileStructure file = new FileStructure(getFile());
    Block block_from_FileStructure = file.getBlock(0).getBlock();
    
    assertEquals(block_after_append, block_after_reopen);
    assertEquals(block_after_reopen, block_from_FileStructure);
  }
  
  public void test_ObjectInfo_has_right_currentBlock() {
    // save o1 and o2 in block1
    beginUpdate();
    IObjectId o1 = save(new Object());
    IObjectId o2 = save(new Object());
    endUpdate();
    
    Block block1 = getBlockManager().getBlock(0);
    assertEquals(block1, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block1, getObjectInfo(o2).getCurrentBlock());
    
    // update o2 to block2
    save(get(o2));
    Block block2 = getBlockManager().getBlock(1);
    assertEquals(block1, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block2, getObjectInfo(o2).getCurrentBlock());
    
    // after reopen, the referenced blocks must still be equals.
    reopen();
    assertEquals(block1, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block2, getObjectInfo(o2).getCurrentBlock());
  }
  
  /**
   * This test required:
   * - fCleanUpThreshold: 50%
   * - fSizeThreshold: 50%
   */
  public void test_RecycleList() {
    
  }
  
  public void test_scenario() {
    
    beginUpdate();
    IObjectId o1 = save(new Object());
    IObjectId o2 = save(new Object());
    endUpdate();
    
    assertEquals(1, getBlockManager().getBlockCount());
    
    Block block1 = getBlockManager().getBlock(0);
    
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(0, block1.getInactiveObjectDataCount());
    assertEquals(block1, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block1, getObjectInfo(o2).getCurrentBlock());
    
    // o2 is saved again in block2
    save(get(o2));
    Block block2 = getBlockManager().getBlock(1);
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(1, block1.getInactiveObjectDataCount());
    assertEquals(1, block2.getObjectDataCount());
    assertEquals(0, block2.getInactiveObjectDataCount());
    
    assertEquals(block1, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block2, getObjectInfo(o2).getCurrentBlock());
    
    reopen();
    
    assertEquals(block1, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block2, getObjectInfo(o2).getCurrentBlock());
    
    // o1 and o2 are saved in block3
    beginUpdate();
    save(get(o1));
    save(get(o2));
    endUpdate();
    
    block1 = getBlockManager().getBlock(0);
    block2 = getBlockManager().getBlock(1);
    Block block3 = getBlockManager().getBlock(2);
    
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(2, block1.getInactiveObjectDataCount());
    assertEquals(1, block2.getObjectDataCount());
    assertEquals(1, block2.getInactiveObjectDataCount());
    assertEquals(2, block3.getObjectDataCount());
    assertEquals(0, block3.getInactiveObjectDataCount());
    
    reopen();
    assertEquals(block3, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block3, getObjectInfo(o2).getCurrentBlock());
    
    block1 = getBlockManager().getBlock(0);
    block2 = getBlockManager().getBlock(1);
    
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(2, block1.getInactiveObjectDataCount());
    assertEquals(1, block2.getObjectDataCount());
    assertEquals(1, block2.getInactiveObjectDataCount());
    assertEquals(2, block3.getObjectDataCount());
    assertEquals(0, block3.getInactiveObjectDataCount());
    
    // deletion-marker for o1 in block4
    delete(get(o1));
    block3 = getBlockManager().getBlock(2);
    Block block4 = getBlockManager().getBlock(3);
    
    assertEquals(block4, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block3, getObjectInfo(o2).getCurrentBlock());
    
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(2, block1.getInactiveObjectDataCount());
    assertEquals(1, block2.getObjectDataCount());
    assertEquals(1, block2.getInactiveObjectDataCount());
    assertEquals(2, block3.getObjectDataCount());
    assertEquals(1, block3.getInactiveObjectDataCount());
    assertEquals(1, block4.getObjectDataCount());
    assertEquals(0, block4.getInactiveObjectDataCount());
    
    assertTrue(getObjectInfo(o1).isDeleted());
    assertFalse(getObjectInfo(o2).isDeleted());
    
    // deletion-marker for o2 in block5
    delete(get(o2));
    Block block5 = getBlockManager().getBlock(4);
    
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(2, block1.getInactiveObjectDataCount());
    assertEquals(1, block2.getObjectDataCount());
    assertEquals(1, block2.getInactiveObjectDataCount());
    assertEquals(2, block3.getObjectDataCount());
    assertEquals(2, block3.getInactiveObjectDataCount());
    assertEquals(1, block4.getObjectDataCount());
    assertEquals(0, block4.getInactiveObjectDataCount());
    assertEquals(1, block5.getObjectDataCount());
    assertEquals(0, block5.getInactiveObjectDataCount());
    assertEquals(block4, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block5, getObjectInfo(o2).getCurrentBlock());
    
    assertTrue(getObjectInfo(o1).isDeleted());
    assertTrue(getObjectInfo(o2).isDeleted());
    
    reopen();
    block1 = getBlockManager().getBlock(0);
    block2 = getBlockManager().getBlock(1);
    block3 = getBlockManager().getBlock(2);
    
    assertEquals(block4, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block5, getObjectInfo(o2).getCurrentBlock());
    
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(2, block1.getInactiveObjectDataCount());
    assertEquals(1, block2.getObjectDataCount());
    assertEquals(1, block2.getInactiveObjectDataCount());
    assertEquals(2, block3.getObjectDataCount());
    assertEquals(2, block3.getInactiveObjectDataCount());
    
    assertTrue(getObjectInfo(o1).isDeleted());
    assertTrue(getObjectInfo(o2).isDeleted());
  }
}
