/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.block.maintenancefree;

import org.memoriadb.block.Block;
import org.memoriadb.core.block.IBlockManagerExt;
import org.memoriadb.core.exception.MemoriaException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MaintenanceFreeBlockManager implements IBlockManagerExt {

  private final int fInactiveThreshold;
  private final int fSizeThreshold;
  private final List<Block> fBlocks = new ArrayList<Block>();
  private final TreeSet<BlockBucket> fRecycleList = new TreeSet<BlockBucket>(new BlockBucketComparator());

  /**
   * 
   * Recommandation: A inactiveRatio equal or below 50% can lead to very poor performance, because the algorithm may not
   * converge.
   * 
   * @param inactiveThreshold
   *          0..100%. 0 means: at leat on ObjectData must be inactive. 100 means: all (100%) of the ObjectDatas must be
   *          inactive
   * 
   * @param sizeThreshold
   *          0..100%: 0 means: every block qualifies for recycling, 100% means: A block only qualifies for recycling
   *          when it's size matches the requested size.
   */
  public MaintenanceFreeBlockManager(int inactiveThreshold, int sizeThreshold) {
    checkIsPercent(inactiveThreshold);
    checkIsPercent(sizeThreshold);

    fInactiveThreshold = inactiveThreshold;
    fSizeThreshold = sizeThreshold;
  }

  @Override
  public void add(Block block) {
    fBlocks.add(block);
    block.setBlockManager(this);
    inactiveRatioChanged(block);
  }

  @Override
  public Block allocatedRecyclebleBlock(long blockSize, Set<Block> tabooBlocks) {
    // create a block as search-prototype

    long currentSize = blockSize;

    while (true) {
      BlockBucket blockBucket = fRecycleList.ceiling(new BlockBucket(currentSize));
      if (blockBucket == null) return null; // no block with the requested size in the recycle list.

      long ratio = currentSize * 100 / blockBucket.getSize();
      if (ratio >= fSizeThreshold) {
        Block result = getBlock(blockBucket, tabooBlocks);
        if (blockBucket.isEmpty()) fRecycleList.remove(blockBucket);
        if (result != null) return result;
      }
      else {
        // sizeThreshold exceeded
        return null;
      }

      
      // This guarantees that the algorithm does not stop making progress. 
      // If a BlockBucket is found with the right size but all it's Blocks are
      // contained in the taboo-list, the next bigger BlockBucket must be taken to check if
      // it's size still fits in the given sizeThreshold.
      currentSize = Math.max(currentSize + 1, blockBucket.getSize());
    }
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
    for (BlockBucket bucket : fRecycleList) {
      result += bucket.getBlockCount();
    }
    return result;
  }

  @Override
  public void inactiveRatioChanged(Block block) {
    if (!blockQualifiesForRecycling(block)) return;

    BlockBucket prototype = new BlockBucket(block.getBodySize());
    BlockBucket bucket = fRecycleList.ceiling(prototype);
    if (bucket == null || bucket.getSize() != block.getBodySize()) {
      // no BlockBucket with exactly the requested bodySize was found. Add the created prototype.
      bucket = prototype;
      fRecycleList.add(bucket);
    }

    bucket.add(block);
  }

  @Override
  public long getBlockSize(final int length) {
    return length;
  }

  private boolean blockQualifiesForRecycling(Block block) {
    if (block.isFree()) return true;

    // if the inactiveThreshold is 0, a single inactive ObjectData qualifies the block for recycling.
    if (fInactiveThreshold == 0) return block.getInactiveObjectDataCount() > 0;

    return block.getInactiveRatio() >= fInactiveThreshold;
  }

  private void checkIsPercent(int value) {
    if (value < 0 || value > 100) throw new MemoriaException("not in [0..100]:" + value);
  }

  private Block getBlock(BlockBucket blockBucket, Set<Block> tabooBlocks) {
    for (Block block : blockBucket.getBlocks()) {
      if (tabooBlocks.contains(block)) continue;
      blockBucket.remove(block);
      return block;
    }
    return null;
  }

}
