package org.memoriadb.core;

import org.memoriadb.core.block.Block;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.util.Constants;

/**
 * Holds all data to an object necessary for internal housekeeping
 * 
 * fObj == null means, that this object has been deleted
 * 
 * @author msc
 * 
 */
public class ObjectInfo implements IObjectInfo {
  
  private Object fObj;
  private final IObjectId fId;
  private final IObjectId fMemoriaClassId;
  private long fVersion;
  private int fOldGenerationCount;
  private Block fCurrentBlock;
  
  /**
   * From this block the active HydratedObject comes from
   */
  private Block fBlock;
  
  /**
   * Use this ctor only when an object is initially added to the container.
   */
  public ObjectInfo(IObjectId id, IObjectId memoriaClassId, Object obj, Block currentBlock) {
    this(id, memoriaClassId, obj, currentBlock,  Constants.INITIAL_VERSION, 0);
  }

  /**
   * Use this ctor for ojects after dehydration
   */
  public ObjectInfo(IObjectId id, IObjectId memoriaClassId, Object obj, Block currentBlock, long version, int oldGenerationCount) {
    fObj = obj;
    fId = id;
    fCurrentBlock = currentBlock;
    fMemoriaClassId = memoriaClassId;
    fVersion = version;
    fOldGenerationCount = oldGenerationCount;
  }

  @Override
  public void changeCurrentBlock(Block block) {
    fCurrentBlock = block;
  }

  public Block getBlock() {
    return fBlock;
  }

  @Override
  public Object getCurrentBlock() {
    return fCurrentBlock;
  }

  public IObjectId getId(){
    return fId;
  }

  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  public Object getObj() {
    return fObj;
  }

  public int getOldGenerationCount() {
    return fOldGenerationCount;
  }

  public long getVersion() {
    return fVersion;
  }

  public void incrememntOldGenerationCount() {
    ++fOldGenerationCount;
  }

  public void incrementVersion() {
    ++fVersion;
  }
  
  public boolean isDeleted() {
    return fObj == null;
  }
  
  public void setBlock(Block block) {
    fBlock = block;
  }

  public void setDeleted() {
    fObj = null;
  }

  public void setObj(Object obj) {
    fObj = obj;
  }

  public void setVersion(int version) {
    fVersion = version;
  }
  
  @Override
  public String toString() {
    return fId + ":" + fObj + " in revision " + fVersion;
  }
  
}
