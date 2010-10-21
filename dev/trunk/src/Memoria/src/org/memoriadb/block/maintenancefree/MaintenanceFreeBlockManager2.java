/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.block.maintenancefree;

import org.memoriadb.block.Block;
import org.memoriadb.block.BlockManagerUtil;
import org.memoriadb.core.block.IBlockManagerExt;
import org.memoriadb.core.exception.MemoriaException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Sandro
 */
public final class MaintenanceFreeBlockManager2  implements IBlockManagerExt {

  private final int fInactiveThreshold;
  private final List<Block> fBlocks = new ArrayList<Block>();
  private final ArrayList<List<Block>> fRecycleList = new ArrayList<List<Block>>(8);

  /**
   *
   * Recommandation: A inactiveRatio equal or below 50% can lead to very poor performance, because the algorithm may not
   * come to an end findind a block.
   *
   * @param inactiveThreshold
   *          0..100%. 0 means: at leat on ObjectData must be inactive. 100% means: all (100%) of the ObjectDatas must be
   *          inactive
   *
   */
  public MaintenanceFreeBlockManager2(int inactiveThreshold) {
    checkIsPercent(inactiveThreshold);

    fInactiveThreshold = inactiveThreshold;
  }

  @Override
  public void add(Block block) {
    fBlocks.add(block);
    block.setBlockManager(this);
    inactiveRatioChanged(block);
  }

  @Override
  public Block allocatedRecyclebleBlock(long blockSize, Set<Block> tabooBlocks) {
    long currentSize = BlockManagerUtil.getNextAlignedBlockSize(blockSize);
    int index = BlockManagerUtil.getIndexForAlignedBlockSize(currentSize);
    if (index >= fRecycleList.size()) return null;
    
    List<Block> blocks = fRecycleList.get(index);
    if (blocks == null) return null;

    if (blocks.isEmpty()) return null;
    Block block = blocks.remove(0);
    if (block.getBodySize() != currentSize) throw new MemoriaException("wrong block for size. needed block size: " + currentSize + " block: " +block.getBodySize());
    return block;
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
    int result = 0;
    for (List<Block> blocks : fRecycleList) {
      if (blocks == null) continue;
      result += blocks.size();
    }
    return result;
  }

  @Override
  public void inactiveRatioChanged(Block block) {
    if (!blockQualifiesForRecycling(block)) return;

    int index = BlockManagerUtil.getIndexForAlignedBlockSize(block.getBodySize());
    ensureCapacity(index);

    List<Block> blocks = fRecycleList.get(index);
    if (blocks == null) {
      blocks = new ArrayList<Block>();
      fRecycleList.set(index, blocks);
    }
    
    blocks.add(block);
  }

  @Override
  public long getBlockSize(final int bodySize) {
    return BlockManagerUtil.getNextAlignedBlockSize(bodySize);
  }

  private void ensureCapacity(final int index) {
    int sizeDiff = index - fRecycleList.size() + 1;
    if (sizeDiff < 0) return;
    if (sizeDiff == 0) sizeDiff = 2;

    int elementsToAdd = (int)BlockManagerUtil.getNextPowerOfTwo(sizeDiff);
    for(int i = 0; i < elementsToAdd; ++i) {
      fRecycleList.add(null);
    }
  }

  private boolean blockQualifiesForRecycling(Block block) {
    if (block.isFree()) return true;

    // if the inactiveThreshold is 0, a single inactive Object qualifies the block for recycling.
    if (fInactiveThreshold == 0) return block.getInactiveObjectDataCount() > 0;

    return block.getInactiveRatio() >= fInactiveThreshold;
  }

  private void checkIsPercent(int value) {
    if (value < 0 || value > 100) throw new MemoriaException("not in [0..100]:" + value);
  }

}
