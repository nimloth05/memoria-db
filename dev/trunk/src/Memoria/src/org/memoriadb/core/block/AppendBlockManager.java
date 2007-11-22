package org.memoriadb.core.block;

import java.util.*;

/**
 * Never recycles a block. For each transaction, a new block is appended to the file.
 * Basically, this BlockManager keeps the history of a db and allowes restoring older revisions.
 *  
 * @author msc
 */
public class AppendBlockManager implements IBlockManagerExt {

  private final List<Block> fBlocks = new ArrayList<Block>();
  
  @Override
  public void add(Block block) {
    fBlocks.add(block);
  }

  @Override
  public Block allocatedRecyclebleBlock(long blockSize, Set<Block> tabooBlocks) {
    // never recycle a block
    return null;
  }

  @Override
  public Block getBlock(int index) {
    return fBlocks.get(index);
  }

  @Override
  public int getBlockCount() {
    return fBlocks.size();
  }

  @Override
  public int getRecyclingBlockCount() {
    return 0;
  }

  @Override
  public void inactiveRatioChanged(Block block) {
  }

}
