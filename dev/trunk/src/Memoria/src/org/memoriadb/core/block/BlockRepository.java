package org.memoriadb.core.block;

import java.util.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.*;
import org.memoriadb.core.util.collection.MultiMapOrdered;

public final class BlockRepository {

  private final MultiMapOrdered<Block, IObjectInfo> fBlockToObjectInfos = new MultiMapOrdered<Block, IObjectInfo>();
  private final Map<IObjectInfo, Block> fObjectInfoToBlock = new HashMap<IObjectInfo, Block>();
  
  public void add(IObjectInfo objectInfo, Block block) {
    fBlockToObjectInfos.put(block, objectInfo);
    fObjectInfoToBlock.put(objectInfo, block);
  }
  
  public Block getBlock(IObjectInfo objectInfo) {
    return fObjectInfoToBlock.get(objectInfo);
  }

  public List<IObjectInfo> getObjectInfos(Block block) {
    return fBlockToObjectInfos.get(block);
  }
  
  public Block remove(ObjectInfo objectInfo) {
    Block oldBlock = fObjectInfoToBlock.remove(objectInfo);
    if (oldBlock == null) return null;
    fBlockToObjectInfos.remove(oldBlock, objectInfo);
    return oldBlock;
  }

  public Block update(ObjectInfo objectInfo, Block newBlock) {
    Block oldBlock = remove(objectInfo);
    add(objectInfo, newBlock);
    return oldBlock;
  }
  
}
