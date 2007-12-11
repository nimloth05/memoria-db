package org.memoriadb.test.block;

import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.CreateConfig;
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
    
    save(o1);
    beginUpdate();
      delete(o1);
      save(o2);
    endUpdate();
    // |~o1|d1,o2|
    
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(100, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(0, getBlockManager().getBlock(2).getInactiveRatio());
    
    save(o2);
    // |o2'|d1,~o2|
    
    assertEquals(3, getBlockManager().getBlockCount());
    assertEquals(0, getBlockManager().getBlock(1).getInactiveRatio());
    assertEquals(100, getBlockManager().getBlock(2).getInactiveRatio());
    
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
