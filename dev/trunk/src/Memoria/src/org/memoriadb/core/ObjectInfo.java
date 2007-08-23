package org.memoriadb.core;

import org.memoriadb.core.block.Block;
import org.memoriadb.exception.MemoriaException;

/**
 * Hält alle relevanten Informationen zu einem von Memoria verwalteten Objekt
 * 
 * @author msc
 * 
 */
public class ObjectInfo {
  
  private Object fObj;
  private final long fId;
  private long fVersion;
  private boolean fIsDeleted;
  private boolean fHasInactiveObjectData;
  
  /**
   * From this block the active HydratedObject comes from
   */
  private Block fBlock;
  
  /**
   * Use this ctor only when an object is initially added to the container.
   */
  public ObjectInfo(long id, Object obj) {
    this(id, obj, 0);
  }

  /**
   * Use this ctor for ojects after dehydration
   */
  public ObjectInfo(long id, Object obj, long version) {
    if(obj == null) throw new MemoriaException("null can not be stored for id " + id);
    
    fObj = obj;
    fId = id;
    fVersion = version;
    fIsDeleted = false;
    fHasInactiveObjectData = false;
  }

  public Block getBlock() {
    return fBlock;
  }

  public long getId(){
    return fId;
  }

  public Object getObj() {
    return fObj;
  }

  public long getVersion() {
    return fVersion;
  }

  public void incrementVersion() {
    ++fVersion;
  }

  public boolean isDeleted() {
    return fIsDeleted;
  }

  public boolean isHasInactiveObjectData() {
    return fHasInactiveObjectData;
  }

  public void setBlock(Block block) {
    fBlock = block;
  }

  public void setDeleted(boolean isDeleted) {
    fIsDeleted = isDeleted;
  }

  public void setHasInactiveObjectData(boolean hasInactiveObjectData) {
    fHasInactiveObjectData = hasInactiveObjectData;
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
