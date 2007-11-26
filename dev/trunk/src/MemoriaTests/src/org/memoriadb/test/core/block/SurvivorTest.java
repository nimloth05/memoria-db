package org.memoriadb.test.core.block;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.block.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.testutil.AbstractObjectStoreTest;

/**
 * Tests the reuse of blocks
 * 
 * @author msc
 * 
 */
public class SurvivorTest extends AbstractObjectStoreTest {

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
  
  public void test_reusing_one_block() {
    Object o1 = new Object();
    IObjectId id1 = save(o1);
    assertEquals(2, getBlockManager().getBlockCount());
    
    reopen();
    
    // deleting the object, waht results in a free block and a  deletionMarker.
    delete(get(id1));
    
    // let o2 reuse the free block
    IObjectId id2 = save(new Object());
    
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(getBlock(1), getCurrentBlock(id2));
    assertEquals(getBlock(2), getCurrentBlock(id1));
    assertTrue(getObjectInfo(id1).isDeleted());
    assertFalse(getObjectInfo(id2).isDeleted());
    assertFalse(fObjectStore.containsId(id1));
    assertTrue(fObjectStore.containsId(id2));
    
    reopen();
    
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(getBlock(1), getCurrentBlock(id2));
    assertEquals(getBlock(2), getCurrentBlock(id1));
    assertTrue(getObjectInfo(id1).isDeleted());
    assertFalse(getObjectInfo(id2).isDeleted());
    assertFalse(fObjectStore.containsId(id1));
    assertTrue(fObjectStore.containsId(id2));
  }
  
  public void test_safing_survivor() {
    
    beginUpdate();
      IObjectId id1 = save(new Object());
      IObjectId id2 = save(new Object());
    endUpdate();
    
    // second block, block 0 has one inactive, one active.
    delete(get(id1));
    
    assertEquals(3, getObjectInfo(id1).getRevision());
    assertEquals(2, getObjectInfo(id2).getRevision());
    
    assertEquals(50, getBlock(1).getInactiveRatio());
    assertEquals(0, getBlock(2).getInactiveRatio());
    
    IObjectId id3 = save(new Object());

    // test trx-id is proper
    assertEquals(3, getObjectInfo(id1).getRevision());
    assertEquals(4, getObjectInfo(id2).getRevision());
    assertEquals(5, getObjectInfo(id3).getRevision());
    
    assertFalse(fObjectStore.containsId(id1));
    assertTrue(fObjectStore.containsId(id2));
    assertTrue(fObjectStore.containsId(id3));

    assertEquals(4, getBlockManager().getBlockCount());
    assertEquals(getBlock(1), getCurrentBlock(id3));
    assertEquals(getBlock(2), getCurrentBlock(id1));
    assertEquals(getBlock(3), getCurrentBlock(id2));
    
    reopen();

    assertFalse(fObjectStore.containsId(id1));
    assertTrue(fObjectStore.containsId(id2));
    assertTrue(fObjectStore.containsId(id3));

    assertEquals(4, getBlockManager().getBlockCount());
    assertEquals(getBlock(1), getCurrentBlock(id3));
    assertEquals(getBlock(2), getCurrentBlock(id1));
    assertEquals(getBlock(3), getCurrentBlock(id2));
    
    assertEquals(0, getBlock(1).getInactiveRatio());
    assertEquals(0, getBlock(2).getInactiveRatio());
    assertEquals(0, getBlock(3).getInactiveRatio());
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

  /**
   * @param index Block from BlockManager
   */
  private Block getBlock(int index) {
    return getBlockManager().getBlock(index);
  }

  private Block getCurrentBlock(IObjectId id) {
    return getObjectInfo(id).getCurrentBlock();
  }

}

