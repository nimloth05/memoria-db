package org.memoriadb.core;

import org.memoriadb.core.backend.Block;

/**
 * Hält alle relevanten Informationen zu einem von Memoria verwalteten Objekt
 * 
 * @author msc
 * 
 */
public class ObjectInfo {
  
  private Object fObj;
  
  /**
   * During loading the DB file, the HydratedObject is stored in this field.
   */
  private HydratedObject fHydratedObject;

  private int fVersion;
  private boolean fIsDeleted;
  private boolean fHasInactiveObjectData;
  
  /**
   * From this block the active HydratedObject comes from
   */
  private Block fBlock;
  
  public ObjectInfo() {
    
  }

  public Block getBlock() {
    return fBlock;
  }

  public HydratedObject getHydratedObject() {
    return fHydratedObject;
  }

  public Object getObj() {
    return fObj;
  }

  public int getVersion() {
    return fVersion;
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

  public void setHydratedObject(HydratedObject hydratedObject) {
    fHydratedObject = hydratedObject;
  }

  public void setObj(Object obj) {
    fObj = obj;
  }

  public void setVersion(int version) {
    fVersion = version;
  }
  
}
