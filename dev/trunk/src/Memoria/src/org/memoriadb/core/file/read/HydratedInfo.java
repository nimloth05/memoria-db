/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.core.file.read;

import org.memoriadb.block.Block;
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
  private int fOldGenerationCount;
  private Block fCurrentBlock;
  private IObjectId fTypeId;
  
  public HydratedInfo(IObjectId id, IObjectId typeId, HydratedObject hydratedObject, Block currentBlock) {
    fId = id;
    fTypeId = typeId;
    fHydratedObject = hydratedObject;
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
  public void update(Block block, HydratedObject hydratedObject, IObjectId typeId) {
    // es kann nicht 2 mal dieselbe Version abgespeichert sein
    //if(fVersion == version) throw new MemoriaException("two object-data have same revision: " + version);

    ++fOldGenerationCount;
    
    // if the current version is bigger than the read one, adjust the block where the inactive ObjectData comes from.
    if(fCurrentBlock.getRevision() > block.getRevision()){
      block.incrementInactiveObjectDataCount();
      return;
    }

    fHydratedObject = hydratedObject;
    fTypeId = typeId;
    
    // update current block before changing to the new block
    fCurrentBlock.incrementInactiveObjectDataCount();
    
    fCurrentBlock = block;
  }
  
}
