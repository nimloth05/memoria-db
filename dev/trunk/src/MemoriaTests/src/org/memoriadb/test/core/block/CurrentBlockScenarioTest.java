package org.memoriadb.test.core.block;

import org.memoriadb.block.*;
import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.block.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class CurrentBlockScenarioTest extends AbstractMemoriaTest {

  public void test_current_block() {
    assertTrue(getLastBlockInfo().isAppend());
    assertFalse(getLastBlockInfo().hasLastWrittenBlock());
    
    save(new Object());
    
    // header-info is never updated, reopen the db
    reopen();
    
    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(1).getPosition(), getLastBlockInfo().getPosition());
  }

  public void test_scenario() {
    
    beginUpdate();
    IObjectId o1 = save(new Object());
    IObjectId o2 = save(new Object());
    endUpdate();
    
    assertEquals(2, getBlockManager().getBlockCount());
    
    Block block1 = getBlockManager().getBlock(1);
    
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(0, block1.getInactiveObjectDataCount());
    assertEquals(block1, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block1, getObjectInfo(o2).getCurrentBlock());
    
    // o2 is saved again in block2
    save(get(o2));
    Block block2 = getBlockManager().getBlock(2);
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
    
    block1 = getBlockManager().getBlock(1);
    block2 = getBlockManager().getBlock(2);
    Block block3 = getBlockManager().getBlock(3);
    
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(2, block1.getInactiveObjectDataCount());
    assertEquals(1, block2.getObjectDataCount());
    assertEquals(1, block2.getInactiveObjectDataCount());
    assertEquals(2, block3.getObjectDataCount());
    assertEquals(0, block3.getInactiveObjectDataCount());
    
    reopen();
    assertEquals(block3, getObjectInfo(o1).getCurrentBlock());
    assertEquals(block3, getObjectInfo(o2).getCurrentBlock());
    
    block1 = getBlockManager().getBlock(1);
    block2 = getBlockManager().getBlock(2);
    
    assertEquals(2, block1.getObjectDataCount());
    assertEquals(2, block1.getInactiveObjectDataCount());
    assertEquals(1, block2.getObjectDataCount());
    assertEquals(1, block2.getInactiveObjectDataCount());
    assertEquals(2, block3.getObjectDataCount());
    assertEquals(0, block3.getInactiveObjectDataCount());
    
    // deletion-marker for o1 in block4
    delete(get(o1));
    block3 = getBlockManager().getBlock(3);
    Block block4 = getBlockManager().getBlock(4);
    
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
    Block block5 = getBlockManager().getBlock(5);
    
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
    block1 = getBlockManager().getBlock(1);
    block2 = getBlockManager().getBlock(2);
    block3 = getBlockManager().getBlock(3);
    
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
  
  @Override
  protected void configureOpen(CreateConfig config) {
    config.setBlockManager(new AppendBlockManager());
  }

  @Override
  protected void configureReopen(CreateConfig config) {
    config.setBlockManager(new AppendBlockManager());
  }
  
}
