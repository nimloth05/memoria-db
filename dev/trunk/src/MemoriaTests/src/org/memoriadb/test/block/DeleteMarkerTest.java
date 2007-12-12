package org.memoriadb.test.block;

import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.CreateConfig;
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

    IObjectId id1 = save(o1);
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

}
