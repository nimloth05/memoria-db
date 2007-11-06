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
  private long fVersion;
  private int fOldGenerationCount;
  
  /**
   * From this block the active HydratedObject comes from
   */
  private Block fBlock;
  
  /**
   * Use this ctor only when an object is initially added to the container.
   */
  public ObjectInfo(IObjectId id, Object obj) {
    this(id, obj, Constants.INITIAL_VERSION, 0);
  }

  /**
   * Use this ctor for ojects after dehydration
   */
  public ObjectInfo(IObjectId id, Object obj, long version, int oldGenerationCount) {
    fObj = obj;
    fId = id;
    fVersion = version;
    fOldGenerationCount = oldGenerationCount;
  }

  public Block getBlock() {
    return fBlock;
  }

  public IObjectId getId(){
    return fId;
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
