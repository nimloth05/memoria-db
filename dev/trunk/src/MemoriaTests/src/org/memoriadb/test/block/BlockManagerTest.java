package org.memoriadb.test.block;

import org.memoriadb.block.Block;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.*;

public class BlockManagerTest extends AbstractMemoriaTest {
  
  /**
   * Test that the Blocks are created properly, 
   * no matter if the file is initially read or the Block is appended while running.
   */
  public void test_blocks_are_created() {
    save(new Object());
    
    Block block_after_append = getBlockFromManager(1);
    
    reopen();
    
    Block block_after_reopen = getBlockFromManager(1);
    
    FileStructure file = new FileStructure(getFile());
    Block block_from_FileStructure = file.getBlock(1).getBlock();
    
    assertBlocks(block_after_append, block_after_reopen);
    assertBlocks(block_after_reopen, block_from_FileStructure);
  }
  
  public void test_ObjectInfo_has_right_currentBlock() {
    // save o1 and o2 in block1
    beginUpdate();
    IObjectId o1 = save(new Object());
    IObjectId o2 = save(new Object());
    endUpdate();
    
    Block block1 = getBlockFromManager(1);
    assertBlocks(block1, getObjectInfo(o1).getCurrentBlock());
    assertBlocks(block1, getObjectInfo(o2).getCurrentBlock());
    
    // update o2 to block2
    save(get(o2));
    Block block2 = getBlockManager().getBlock(2);
    assertBlocks(block1, getObjectInfo(o1).getCurrentBlock());
    assertBlocks(block2, getObjectInfo(o2).getCurrentBlock());
    
    // after reopen, the referenced blocks must still be equals.
    reopen();
    assertBlocks(block1, getObjectInfo(o1).getCurrentBlock());
    assertBlocks(block2, getObjectInfo(o2).getCurrentBlock());
  }

  private Block getBlockFromManager(int index) {
    return getBlockManager().getBlock(index);
  }
  
}
