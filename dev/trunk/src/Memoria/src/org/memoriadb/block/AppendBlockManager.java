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

package org.memoriadb.block;

import org.memoriadb.core.block.IBlockManagerExt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Never recycles a block. For each transaction, a new block is appended to the file.
 * Basically, this BlockManager keeps the history of a db and allowes restoring older revisions.
 *  
 * @author Sandro Orlando
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
