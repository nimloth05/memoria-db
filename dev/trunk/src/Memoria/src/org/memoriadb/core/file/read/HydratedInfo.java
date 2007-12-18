package org.memoriadb.core.file.read;

import org.memoriadb.block.Block;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.id.IObjectId;

/**
 * 
 * fHydratedObject == null means that this object has been deleted.
 * 
 * @author msc
 */
public class HydratedInfo {
  
  private final IObjectId fId;
  private HydratedObject fHydratedObject;
  private long fVersion;
  private int fOldGenerationCount;
  private Block fCurrentBlock;
  private IObjectId fTypeId;
  
  public HydratedInfo(IObjectId id, IObjectId typeId, HydratedObject hydratedObject, long version, Block currentBlock) {
    fId = id;
    fTypeId = typeId;
    fHydratedObject = hydratedObject;
    fVersion = version;
    fCurrentBlock = currentBlock;
    fOldGenerationCount = 0;
  }

  public Block getCurrentBlock() {
    return fCurrentBlock;
  }

  public IObjectId getMemoriaClassId() {
    return fTypeId;
  }

  /**
   * @return the dehydrated object or null, if the object has been deleted.
   * @throws Exception 
   */
  public Object getObject(IReaderContext context) throws Exception {
    if(fHydratedObject == null) return null;
    return fHydratedObject.dehydrate(context);
  }

  public IObjectId getObjectId() {
    return fId;
  }

  public int getOldGenerationCount() {
    return fOldGenerationCount;
  }
  
  public long getVersion() {
    return fVersion;
  }

  public boolean isDeleted() {
    return fHydratedObject == null;
  }

  @Override
  public String toString() {
    return "HydratedObject: " + fId.toString() + (isDeleted()?" deleted":"");
  }
  
  /**
   * @param hydratedObject pass null to mark this object as deleted
   */
  public void update(Block block, HydratedObject hydratedObject, IObjectId typeId, long version) {
    // es kann nicht 2 mal dieselbe Version abgespeichert sein
    if(fVersion == version) throw new MemoriaException("two object-data have same revision: " + version);
    
    ++fOldGenerationCount;
    
    // if the current version is bigger than the read one, adjust the block where the inactive ObjectData comes from.
    if(fVersion > version){
      block.incrementInactiveObjectDataCount();
      return;
    }
    
    fVersion = version;
    fHydratedObject = hydratedObject;
    fTypeId = typeId;
    
    // update current block before changing to the new block
    fCurrentBlock.incrementInactiveObjectDataCount();
    
    fCurrentBlock = block;
  }
  
}