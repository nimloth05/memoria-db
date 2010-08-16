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

package org.memoriadb.block;

import java.util.Set;

/**
 * @author msc
 */
public interface IBlockManager {
  
  /**
   * Called when objects are read in.
   */
  public void add(Block block);
  
  /**
   * ATTENTION: The returned block may contain survivors!
   * 
   * Returns a block that maches the requirements or null, if a new block should be appended.
   * 
   * @param blockSize The net-size of the required block.
   * @return A block which has at least <tt>blockSize</tt> (the block still may contain survivors) 
   * or null, if no block met the requirements.  
   */
  public Block allocatedRecyclebleBlock(long blockSize, Set<Block> tabooBlocks);

  /**
   * Called when the inactiveObjectDataRatio of a block changed.
   */
  public void inactiveRatioChanged(Block block);

  public long getBlockSize(int bodySize);
}
