package org.memoriadb.test.core.block;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.block.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.listener.IWriteListener;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class LastWrittenBlockAfterCrashTest extends AbstractObjectStoreTest {

  public void test_crash_after_safing_survivors() {

    beginUpdate();
    IObjectId id1 = save(new Object());
    IObjectId id2 = save(new Object());
    endUpdate();
    
    reopen();

    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(0).getPosition(), getLastBlockInfo().getPosition());

        
    // second block, block 0 has one inactive, one active.
    delete(get(id1));
    
    reopen();
    
    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(1).getPosition(), getLastBlockInfo().getPosition());

    
    // crashes
    try {
      IObjectId id3 = save(new Object());
    }
    catch(MemoriaException e) {
    }

    reopen();
    
    // block 2 is the last block that was written (contains the survivor o2), then the write-process crashed
    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(2).getPosition(), getLastBlockInfo().getPosition());
    
    // survivor in block0 was safed -> no active ObjectData in block0
    assertEquals(2, getBlockManager().getBlock(0).getObjectDataCount());
    assertEquals(2, getBlockManager().getBlock(0).getInactiveObjectDataCount());
    
    // o2 is the survivor that is now in block 2
    assertEquals(getBlockManager().getBlock(2), getObjectInfo(id2).getCurrentBlock());
    assertFalse(getObjectInfo(id2).isDeleted());
    
    assertEquals(getBlockManager().getBlock(1), getObjectInfo(id1).getCurrentBlock());
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
