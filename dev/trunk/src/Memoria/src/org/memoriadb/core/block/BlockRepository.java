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
