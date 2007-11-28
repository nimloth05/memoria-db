package org.memoriadb.test.core.block;

import org.memoriadb.block.Block;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.listener.IWriteListener;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class LastWrittenBlockAfterCrashTest extends AbstractMemoriaTest {

  public void test_crash_after_safing_survivors() {
    
    beginUpdate();
    IObjectId id1 = save(new Object());
    IObjectId id2 = save(new Object());
    endUpdate();
    
    reopen();

    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(1).getPosition(), getLastBlockInfo().getPosition());

    // write block2, block1 has one inactive, one active.
    delete(get(id1));
    
    reopen();
    
    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(2).getPosition(), getLastBlockInfo().getPosition());

    
    // crashes
    try {
      save(new Object());
    }
    catch(MemoriaException e) {
    }

    reopen();
    
    // block 3 is the last block that was writte (contains the survivor o2), then the write-process crashed
    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(3).getPosition(), getLastBlockInfo().getPosition());
    
    // survivor in block1 was safed -> no active ObjectData in block0
    assertEquals(2, getBlockManager().getBlock(1).getObjectDataCount());
    assertEquals(2, getBlockManager().getBlock(1).getInactiveObjectDataCount());
    
    // o2 is the survivor that is now in block3
    assertEquals(getBlockManager().getBlock(3), getObjectInfo(id2).getCurrentBlock());
    assertFalse(getObjectInfo(id2).isDeleted());
    
    assertEquals(getBlockManager().getBlock(2), getObjectInfo(id1).getCurrentBlock());
    assertTrue(getObjectInfo(id1).isDeleted());

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
    config.getListeners().add(new IWriteListener() {

      @Override
      public void afterAppend(Block block) {
      }

      @Override
      public void afterWrite(Block block) {
      }

      @Override
      public void beforeAppend(Block block) {
      }

      @Override
      public void beforeWrite(Block block) {
        throw new MemoriaException("simulate crash after safing survivors");
      }
      
    });
  }
  
}
