package org.memoriadb.core.block;

import java.util.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.*;
import org.memoriadb.id.IObjectId;

public final class BlockRepository {

//  private final MultiMapUnOrdered<Block, IObjectId> fBlockToObjectIds = new MultiMapUnOrdered<Block, IObjectId>();
  private final Map<IObjectId, Block> fObjectIdToCurrentBlock = new HashMap<IObjectId, Block>();
  
  public void add(IObjectId id, Block block) {
//    fBlockToObjectIds.put(block, id);
    fObjectIdToCurrentBlock.put(id, block);
  }
  
  public Block getBlock(IObjectId id) {
    return fObjectIdToCurrentBlock.get(id);
  }
  
  public Block getBlock(IObjectInfo objectInfo) {
    return fObjectIdToCurrentBlock.get(objectInfo.getId());
  }

//  public Set<IObjectId> getObjectInfos(Block block) {
//    return fBlockToObjectIds.get(block);
//  }
  
  public void remove(IObjectId objectId) {
    fObjectIdToCurrentBlock.remove(objectId);
  }

//  public void removeBlock(Block block) {
//    fBlockToObjectIds.remove(block);
//  }
  
  public Block update(IObjectId objectId, Block newBlock) {
    return fObjectIdToCurrentBlock.put(objectId, newBlock);
//    Block oldBlock = fObjectIdToCurrentBlock.get(objectId);
//    add(objectId, newBlock);
//    return oldBlock;
  }

  public Block update(ObjectInfo info, Block block) {
    return update(info.getId(), block);
  }
  
}
