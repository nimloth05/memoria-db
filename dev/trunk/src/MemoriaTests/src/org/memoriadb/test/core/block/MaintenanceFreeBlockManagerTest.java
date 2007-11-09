package org.memoriadb.test.core.block;

import org.memoriadb.core.block.*;

public class MaintenanceFreeBlockManagerTest extends junit.framework.TestCase {
  
  public void test_findRecyclebleBlock() {
    IBlockManager manager = new MaintenanceFreeBlockManager();
    
    Block b10a = new Block(10,0);
    Block b10b = new Block(10,1);
    
    manager.add(b10a);
    manager.add(b10b);
    
  }

}
