package org.memoriadb.test.core.scenario;

import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.CreateConfig;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class BlockReuseTest extends AbstractMemoriaTest {
  public void test_reusing_one_block() {
    Object o1 = new Object();
    IObjectId id1 = save(o1);
    
    reopen();
    
    // deleting the object, waht results in a free block and a deletionMarker.
    delete(get(id1));

    reopen();

    // reuse the free block
    save(new Object());

    reopen();
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
