package org.memoriadb.core;

import org.memoriadb.block.Block;
import org.memoriadb.core.util.Constants;
import org.memoriadb.id.IObjectId;

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
  private long fRevision;
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
    this(id, memoriaClassId, obj, currentBlock,  Constants.INITIAL_HEAD_REVISION, 0);
  }

  /**
   * Use this ctor for ojects after dehydration
   */
  public ObjectInfo(IObjectId id, IObjectId memoriaClassId, Object obj, Block currentBlock, long version, int oldGenerationCount) {
    if (memoriaClassId == null) throw new IllegalArgumentException("MemoriaClassId is null.");
    
    fObj = obj;
    fId = id;
    fCurrentBlock = currentBlock;
    fMemoriaClassId = memoriaClassId;
    fRevision = version;
    fOldGenerationCount = oldGenerationCount;
  }

  @Override
  public void changeCurrentBlock(Block block) {
    fCurrentBlock.incrementInactiveObjectDataCount();
    fCurrentBlock = block;
  }

  public Block getBlock() {
    return fBlock;
  }

  @Override
  public Block getCurrentBlock() {
    return fCurrentBlock;
  }

  public IObjectId getId(){
    return fId;
  }

  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  public Object getObject() {
    return fObj;
  }

  public int getOldGenerationCount() {
    return fOldGenerationCount;
  }

  public long getRevision() {
    return fRevision;
  }

  public void incrememntOldGenerationCount() {
    ++fOldGenerationCount;
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

  public void setRevision(long revision) {
    fRevision = revision;
  }
  
  @Override
  public String toString() {
    if (isDeleted()) return fId + ": DELETED revision: " + fRevision; 
    return fId + ": " + fObj.getClass().getName() + " revision: " + fRevision;
  }
  
}
