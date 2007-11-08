package org.memoriadb.testutil;

import org.memoriadb.core.block.*;

public class BlockManagerDummy implements IBlockManager {

  public static BlockManagerDummy INST = new BlockManagerDummy();
  
  @Override
  public void add(Block block) {
  }

  @Override
  public Block findRecyclebleBlock(int blockSize) {
    return null;
  }

}
