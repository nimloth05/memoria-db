package org.memoriadb.test.core.block;

import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.block.MaintenanceFreeBlockManager;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class LastWrittenBlockTest extends  AbstractObjectStoreTest {
  
  public void test_reusing_one_block() {
    Object o1 = new Object();
    IObjectId id1 = save(o1);
    
    reopen();
    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(0).getPosition(), getLastBlockInfo().getPosition());
    
    // deleting the object, waht results in a free block and a  deletionMarker.
    delete(get(id1));
    
    reopen();
    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(1).getPosition(), getLastBlockInfo().getPosition());
    
    // reuse the free block
    save(new Object());
    
    reopen();
    assertFalse(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(0).getPosition(), getLastBlockInfo().getPosition());
    
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
