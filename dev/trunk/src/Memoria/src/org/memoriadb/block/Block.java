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

import java.util.*;

import org.memoriadb.core.ObjectInfo;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.FileLayout;
import org.memoriadb.id.IObjectId;

/**
 * A Block can not change its position. It can not grow or shrink. It's data can just be moved to another block to
 * make this Block free for new data.
 *
 * The block header is only written once, when the block is appended. including:
 * - block-tag
 * - size
 * - crc over the size
 * 
 * Then follows the body, containing one transaction. The size is the size of the body!
 * FIXME IBlock-Interface einziehen, um die Verwendungs-Schnittstelle von der Konstruktionsschnittstell zu trennen, msc.
 */
public class Block {
  
  /**
   * Number of bytes this block can store.
   * 
   * Limited to int because arrays can't be bigger than INT_MAX (implementation depends on arrays)
   */
  private long fBodySize;
  
  /**
   * Position in the file
   */
  private final long fPosition;
  private IBlockManager fManager;
  
  private final Set<IObjectId> fObjectIds;
  private long fInactiveObjectDataCount;

  // if true, the inactiveRatio fo this block is 100%
  private boolean fIsFree = false;

  public Block(long position) {
    this(-1, position);
  }

  public Block(long bodySize, long position) {
    fBodySize = bodySize;
    fPosition = position;
    fObjectIds = new HashSet<IObjectId>();
    fInactiveObjectDataCount = 0;
  } 

  public void addObjectId(IObjectId objectId) {
    fObjectIds.add(objectId);
    notifyBlockManager();
  }
  
  public void addObjectIds(Iterable<ObjectInfo> objectInfos) {
    for(ObjectInfo objectInfo: objectInfos) {
      fObjectIds.add(objectInfo.getId());
    }
    notifyBlockManager();
  }

  /**
   * @return The net-size of this block
   */
  public long getBodySize() {
    return fBodySize;
  }

  /**
   * @return The position, where the transaction starts (size of the transaction starts here)
   */
  public long getBodyStartPosition() {
    return getPosition() + FileLayout.BLOCK_HEADER_OVERHEAD;
  }

  public long getInactiveObjectDataCount() {
    return fInactiveObjectDataCount;
  }

  /**
   * @return A Value between 0 and 100. 0 Means: no inactive ObjectData, 100 means:
   * 100% of the ObjectData are inactive.
   */
  public long getInactiveRatio() {
    
    // when the block-size still is 0, the ratio is 0
    if(getObjectDataCount() == 0) return 0;
    return fInactiveObjectDataCount*100 / getObjectDataCount();
  }

  public long getObjectDataCount() {
    return fObjectIds.size();
  }

  public Iterable<IObjectId> getObjectIds() {
    return fObjectIds;
  }

  /**
   * @return The absolut position of this block.
   */
  public long getPosition() {
    return fPosition;
  }
  
  public long getWholeSize() {
    return fBodySize + FileLayout.BLOCK_OVERHEAD;
  }

  public void incrementInactiveObjectDataCount() {
    // FIXME experimental
    //if(fInactiveObjectDataCount == fObjectDataCount) return;
    
    ++fInactiveObjectDataCount;
    
    if(fInactiveObjectDataCount > getObjectDataCount()) throw new MemoriaException(String.format("more inactive(%d) than active(%d) ObjectData", fInactiveObjectDataCount, getObjectDataCount()));
    
    notifyBlockManager();
  }
  
  public boolean isFree() {
    return fIsFree;
  }
  
  /**
   * Is called after all survivors were safed.
   */
  public void resetBlock() {
    fInactiveObjectDataCount = 0;
    fObjectIds.clear();
  }

  public void setBlockManager(IBlockManager manager) {
    fManager = manager;
  }

  public void setBodySize(long bodySize) {
    fBodySize = bodySize;
  }

  public void setIsFree() {
    fIsFree  = true;
    notifyBlockManager();
  }

  /**
   * The size of this block (header + body)
   * @param blockSize
   */
  public void setWholeSize(long blockSize) {
    fBodySize = blockSize-FileLayout.BLOCK_OVERHEAD;
  }

  @Override
  public String toString() {
    return "Block ("+fInactiveObjectDataCount+"/"+getObjectDataCount()+") pos:" + getPosition() + " size: " + getBodySize();
  }

  private void notifyBlockManager() {
    if(fManager != null)fManager.inactiveRatioChanged(this);
  }

}
