package org.memoriadb.test.block;

import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.CreateConfig;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class TestInactiveObjectDataCount extends AbstractMemoriaTest {

  public void test() {
    Object o1 = new Object();
    Object o2 = new Object();
    Object o3 = new Object();
    
    beginUpdate();
      IObjectId id1 = save(o1);
      IObjectId id2 = save(o2);
    endUpdate();
    
    beginUpdate();
      delete(o1);
      IObjectId id3 = save(o3);
    endUpdate();
    // |~o1, o2|d1, o3|
    
    assertEquals(1, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id3).getOldGenerationCount());
    
    save(o3);
    // |o3'|d1, ~o3|o2'|
    assertEquals(0, getObjectInfo(id1).getOldGenerationCount());
    assertEquals(0, getObjectInfo(id2).getOldGenerationCount());
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
