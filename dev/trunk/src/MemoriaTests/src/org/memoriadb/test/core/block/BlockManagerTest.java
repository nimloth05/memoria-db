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
  
}