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

package org.memoriadb.core.block;

import java.util.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.*;
import org.memoriadb.id.IObjectId;

public final class BlockRepository {

  private final Map<IObjectId, Block> fObjectIdToCurrentBlock = new HashMap<IObjectId, Block>();
  
  public void add(IObjectId id, Block block) {
    fObjectIdToCurrentBlock.put(id, block);
  }
  
  public Block getBlock(IObjectId id) {
    return fObjectIdToCurrentBlock.get(id);
  }
  
  public Block getBlock(IObjectInfo objectInfo) {
    return fObjectIdToCurrentBlock.get(objectInfo.getId());
  }
  
  public void remove(IObjectId objectId) {
    fObjectIdToCurrentBlock.remove(objectId);
  }

  public Block update(IObjectId objectId, Block newBlock) {
    return fObjectIdToCurrentBlock.put(objectId, newBlock);
  }

  public Block update(ObjectInfo info, Block block) {
    return update(info.getId(), block);
  }
  
}
