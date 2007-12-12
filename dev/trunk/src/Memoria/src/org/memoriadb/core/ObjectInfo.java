package org.memoriadb.core;

import org.memoriadb.block.Block;
import org.memoriadb.core.exception.MemoriaException;
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
  
  /**
   * true, when the deletion-marker for this object has been written
   */
  private boolean fDeleteMarkerPersistent;
  
  /**
   *  null for deleted object
   */
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
    if(obj == null) throw new MemoriaException("new object can not be null");
  }

  /**
   * Use this ctor for ojects after dehydration
   */
  public ObjectInfo(IObjectId id, IObjectId memoriaClassId, Object obj, Block currentBlock, long version, int oldGenerationCount) {
    if (memoriaClassId == null) throw new IllegalArgumentException("MemoriaClassId is null.");
    
    fObj = obj;
    
    // when the object is deleted, there must be a persistent delete-marker
    fDeleteMarkerPersistent = isDeleted();
    
    fId = id;
    fCurrentBlock = currentBlock;
    fMemoriaClassId = memoriaClassId;
    fRevision = version;
    fOldGenerationCount = oldGenerationCount;
  }

  /**
   * Sets the given block and increments the iodc of the former currentBlock. 
   */
  @Override
  public void changeCurrentBlock(Block block) {
    fCurrentBlock.incrementInactiveObjectDataCount();
    setCurrentBlock(block);
  }

  public void decrementOldGenerationCount() {
    --fOldGenerationCount;
    
    if(fOldGenerationCount < 0) throw new MemoriaException("invalid oldgenerationCount: " + fOldGenerationCount);
    
    // FIXME the object-info could now be removed from the index, because no persistent information is left about it.
    if(fOldGenerationCount==0 && fDeleteMarkerPersistent) {
      fCurrentBlock.incrementInactiveObjectDataCount();
    }
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

  public void incrementOldGenerationCount() {
    ++fOldGenerationCount;
  }

  public boolean isDeleted() {
    return fObj == null;
  }
  
  public boolean isDeleteMarkerPersistent() {
    return fDeleteMarkerPersistent;
  }
  
  public void setCurrentBlock(Block block) {
    fCurrentBlock = block;
  }

  public void setDeleted() {
    fObj = null;
  }
  
  public void setDeleteMarkerPersistent() {
    if(!isDeleted()) throw new MemoriaException("object must be deleted before deleteMarker can be persistent");
    fDeleteMarkerPersistent = true;
  }

  public void setObj(Object obj) {
    fObj = obj;
  }
  
  public void setRevision(long revision) {
    fRevision = revision;
  }

  @Override
  public String toString() {
    if (isDeleted()) return fId + " DELETED rev " + fRevision; 
    return fId + " " + fObj.getClass().getName() + " rev " + fRevision;
  }
  
}
