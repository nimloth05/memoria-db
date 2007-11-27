package org.memoriadb.test.core.block;

import java.util.HashSet;

import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class SurvivorAgentTest extends AbstractMemoriaTest {
  
  public void test() {
    beginUpdate();
      Object o1 = new Object();
      IObjectId id1 = save(o1);

      Object o2 = new Object();
      IObjectId id2 = save(o2);
    endUpdate();

    Block b1 = getBlockManager().getBlock(1);
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b1), id1, id2);

    // delte-marker for o1 in b1
    delete(o1);
    Block b2 = getBlockManager().getBlock(2);
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b1), id2);
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b2), id1);

    delete(o2);

    Block b3 = getBlockManager().getBlock(3);
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b1));
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b2), id1);
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b3), id2);
    
    IObjectId o1_g2 = save(o1);
    Block b4 = getBlockManager().getBlock(4);
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b1));
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b2), id1);
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b3), id2);
    org.memoriadb.testutil.Collections.containsAll(getSurvivors(b4), o1_g2);
  }

  @Override
  protected void configureOpen(CreateConfig config) {
    config.setBlockManager(new AppendBlockManager());
  }
  
  @Override
  protected void configureReopen(CreateConfig config) {
    config.setBlockManager(new AppendBlockManager());
  }

  private Iterable<IObjectId> getSurvivors(Block block) {
    HashSet<IObjectId> result = new HashSet<IObjectId>();
    for(IObjectInfo info: fObjectStore.getSurvivors(block)){
      result.add(info.getId());
    }
    return result;
  }
}
