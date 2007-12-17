package org.memoriadb.test.block;

import org.memoriadb.CreateConfig;
import org.memoriadb.block.AppendBlockManager;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

// FIXME experimental
public class SurvivorTestWidthDifferentBlockMgr extends AbstractMemoriaTest {

  public void test() {
    Object o1 = new Object();
    Object o2 = new Object();

    IObjectId id1 = save(o1);

    beginUpdate();
    delete(o1);
    IObjectId id2 = save(o2);
    endUpdate();

    save(o2);
    // |~o1|d1,~o2|o2'|

    // reopen with MaintenanceFreeBM
    reopen();

    Object o3 = new Object();
    Object o4 = new Object();

    assertEquals(4, getBlockManager().getBlockCount());

    beginUpdate();
    IObjectId id3 = save(o3);
    IObjectId id4 = save(o4);
    endUpdate(); // crashed befor the increment of iodc for obsolete DMs was done inside teh recursion.
   
    // |d1'|o3,o4|o2'|
    assertEquals(4, getBlockManager().getBlockCount());
    assertBlocks(getBlock(1), getObjectInfo(id1).getCurrentBlock());
    assertBlocks(getBlock(3), getObjectInfo(id2).getCurrentBlock());
    assertBlocks(getBlock(2), getObjectInfo(id3).getCurrentBlock());
    assertBlocks(getBlock(2), getObjectInfo(id4).getCurrentBlock());
    
    assertTrue(getObjectInfo(id1).isDeleted());
    assertFalse(getObjectInfo(id2).isDeleted());
    assertFalse(getObjectInfo(id3).isDeleted());
    assertFalse(getObjectInfo(id4).isDeleted());
    
    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id3).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id4).getOldGenerationCount());
    
    assertEquals(1, getBlock(1).getObjectDataCount());
    assertEquals(1, getBlock(1).getInactiveObjectDataCount());
    
    assertEquals(2, getBlock(2).getObjectDataCount());
    assertEquals(0, getBlock(2).getInactiveObjectDataCount());

    assertEquals(1, getBlock(3).getObjectDataCount());
    assertEquals(0, getBlock(3).getInactiveObjectDataCount());

    reopen();
    
    assertEquals(4, getBlockManager().getBlockCount());
    assertBlocks(getBlock(1), getObjectInfo(id1).getCurrentBlock());
    assertBlocks(getBlock(3), getObjectInfo(id2).getCurrentBlock());
    assertBlocks(getBlock(2), getObjectInfo(id3).getCurrentBlock());
    assertBlocks(getBlock(2), getObjectInfo(id4).getCurrentBlock());
    
    assertTrue(getObjectInfo(id1).isDeleted());
    assertFalse(getObjectInfo(id2).isDeleted());
    assertFalse(getObjectInfo(id3).isDeleted());
    assertFalse(getObjectInfo(id4).isDeleted());
    
    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id3).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id4).getOldGenerationCount());
    
    assertEquals(1, getBlock(1).getObjectDataCount());
    assertEquals(1, getBlock(1).getInactiveObjectDataCount());
    
    assertEquals(2, getBlock(2).getObjectDataCount());
    assertEquals(0, getBlock(2).getInactiveObjectDataCount());

    assertEquals(1, getBlock(3).getObjectDataCount());
    assertEquals(0, getBlock(3).getInactiveObjectDataCount());
  }

  @Override
  protected void configureOpen(CreateConfig config) {
    config.setBlockManager(new AppendBlockManager());
  }

  @Override
  protected void configureReopen(CreateConfig config) {
    config.setBlockManager(new MaintenanceFreeBlockManager(0, 0));
  }

}
