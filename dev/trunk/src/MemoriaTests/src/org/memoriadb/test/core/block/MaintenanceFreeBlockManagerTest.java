package org.memoriadb.test.core.block;

import org.memoriadb.core.block.*;

public class MaintenanceFreeBlockManagerTest extends junit.framework.TestCase {

  /**
   * One inactive object is enough to make ready for recycling
   */
  public void test_inactiveThreshold_0() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(0, 0);

    Block b10 = new Block(10, 0);
    b10.setNumberOfObjectData(20);
    manager.add(b10);
    
    assertNull(manager.findRecyclebleBlock(11));
    assertNull(manager.findRecyclebleBlock(10));
    
    // now one ObjectData becomes inactive
    b10.incrementInactiveObjectDataCount();

    assertNull(manager.findRecyclebleBlock(11));
    assertNotNull(manager.findRecyclebleBlock(10));
  }
  
  public void test_inactiveThreshold_50_scenario() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(50, 0);

    Block b10a = new Block(10, 0);
    b10a.setNumberOfObjectData(2);
    manager.add(b10a);
    
    assertNull(manager.findRecyclebleBlock(11));
    assertNull(manager.findRecyclebleBlock(10));
    
    b10a.incrementInactiveObjectDataCount();
    assertNull(manager.findRecyclebleBlock(11));
    assertSame(b10a, manager.findRecyclebleBlock(10));
    
    Block b10b = new Block(10, 1);
    b10b.setNumberOfObjectData(2);
    manager.add(b10b);

    assertNull(manager.findRecyclebleBlock(11));
    assertNull(manager.findRecyclebleBlock(10));
    
    b10b.incrementInactiveObjectDataCount();
    assertNull(manager.findRecyclebleBlock(11));
    assertSame(b10b, manager.findRecyclebleBlock(10));
  }
  
  public void test_incativeThreshold_50() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(50, 0);

    Block b10a = new Block(10, 0);
    b10a.setNumberOfObjectData(2);
    manager.add(b10a);

    assertNull(manager.findRecyclebleBlock(20));
    assertNull(manager.findRecyclebleBlock(10));

    b10a.incrementInactiveObjectDataCount();

    assertNull(manager.findRecyclebleBlock(20));
    assertSame(b10a, manager.findRecyclebleBlock(10));

    // block is gone...
    assertNull(manager.findRecyclebleBlock(10));
  }

  /**
   * Two blocks with the same size are added. The set block-Manager uses 50/50 for inactive- and size-Threshold.
   */
  public void test_incativeThreshold_50_two_same_sizes() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(50, 0);

    Block b10a = new Block(10, 0);
    b10a.setNumberOfObjectData(2);

    Block b10b = new Block(10, 1);
    b10b.setNumberOfObjectData(2);

    manager.add(b10b);
    manager.add(b10a);

    assertEquals(2, manager.getBlockCount());
    assertEquals(0, manager.getRecyclingBlockCount());

    // b has inactiveRatio: 1 / 2
    b10a.incrementInactiveObjectDataCount();
    b10b.incrementInactiveObjectDataCount();

    assertEquals(2, manager.getBlockCount());
    assertEquals(2, manager.getRecyclingBlockCount());

    assertNotNull(manager.findRecyclebleBlock(1));
    assertNotNull(manager.findRecyclebleBlock(1));

    // no more blocks for recycling
    assertNull(manager.findRecyclebleBlock(1));
  }

  /**
   * The size doesn't matter
   */
  public void test_sizeThreshold_0() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(0, 0);

    Block b = new Block(1000, 0);
    b.setNumberOfObjectData(20);
    manager.add(b);
    
    b.incrementInactiveObjectDataCount();
    
    assertNull(manager.findRecyclebleBlock(1001));
    assertNotNull(manager.findRecyclebleBlock(1));
  }
  
  /**
   * The block mustn't be bigger than twice the requested size.
   */
  public void test_sizeThreshold_100() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(0, 100);

    Block b = new Block(1000, 0);
    b.setNumberOfObjectData(20);
    manager.add(b);
    
    b.incrementInactiveObjectDataCount();
    
    assertNull(manager.findRecyclebleBlock(1001));
    assertNull(manager.findRecyclebleBlock(999));
    assertNotNull(manager.findRecyclebleBlock(1000));
  }
  
  /**
   * The block mustn't be bigger than twice the requested size.
   */
  public void test_sizeThreshold_50() {
    IBlockManagerExt manager = new MaintenanceFreeBlockManager(0, 50);

    Block b = new Block(1000, 0);
    b.setNumberOfObjectData(20);
    manager.add(b);
    
    b.incrementInactiveObjectDataCount();
    
    assertNull(manager.findRecyclebleBlock(1001));
    assertNull(manager.findRecyclebleBlock(1));
    assertNull(manager.findRecyclebleBlock(499));
    assertNotNull(manager.findRecyclebleBlock(500));
  }

}
