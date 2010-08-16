/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.core.block;

import java.util.Set;

import org.memoriadb.block.Block;
import org.memoriadb.core.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.collection.identity.IdentityHashSet;
import org.memoriadb.id.IObjectId;

/**
 * Computes the survivors in a block.
 * 
 * @author msc
 */
public class SurvivorAgent {
  
  // use IdentityHashSet for better performance
  private final Set<ObjectInfo> fActiveObjectDatas = IdentityHashSet.create();
  private final Set<ObjectInfo> fActiveDeleteMarkers =  IdentityHashSet.create();
  private final Set<ObjectInfo> fInactiveObjectDatas = IdentityHashSet.create();
  private final Set<ObjectInfo> fInactiveDeleteMarkers = IdentityHashSet.create();
  
  private final IObjectRepository fRepo;
  private final BlockRepository fBlockRepo;
  
  public static SurvivorAgent create(IObjectRepository repo, BlockRepository blockRepo, Block block) {
    SurvivorAgent survivorAgent = new  SurvivorAgent(repo, blockRepo, block);
    survivorAgent.computeSurvivors(block);
    return survivorAgent;
  }
  
  protected SurvivorAgent(IObjectRepository repo, BlockRepository blockRepo, Block block) {
    fRepo = repo;
    fBlockRepo = blockRepo;
  }
   
  public void computeSurvivors(Block block) {
    
    for(IObjectId objectId: block.getObjectIds()) {
      ObjectInfo info = fRepo.getObjectInfoForId(objectId);
      if (isDeletionMarker(info, block)) {
        handleDeletionMarker(info, block);
      } else {
        handleObjectData(info, block);
      }
    }
    
    if(block.getObjectDataCount() != getObjectDataCount()) throw new MemoriaException("objectDataCount mismatch. File: " + getObjectDataCount() + " in memory: " + block.getObjectDataCount());
    if(block.getInactiveObjectDataCount() != getInactiveObjectDataCount()) throw new MemoriaException("inactiveObjectDataCount mismatch. File: " + getInactiveObjectDataCount() + " in memory: " + block.getInactiveObjectDataCount());
  }

  public Set<ObjectInfo> getActiveDeleteMarkers() {
    return fActiveDeleteMarkers;
  }

  public Set<ObjectInfo> getActiveObjectData() {
    return fActiveObjectDatas;
  }

  public Set<ObjectInfo> getInactiveDeleteMarkers() {
    return fInactiveDeleteMarkers;
  }

  public Set<ObjectInfo> getInactiveObjectData() {
    return fInactiveObjectDatas;
  }

  public boolean hasSurvivors() {
    return !fActiveObjectDatas.isEmpty() || !fActiveDeleteMarkers.isEmpty();
  }

  private int getInactiveObjectDataCount() {
    return fInactiveDeleteMarkers.size() + fInactiveObjectDatas.size();
  }

  private int getObjectDataCount() {
    return fActiveDeleteMarkers.size() + fInactiveDeleteMarkers.size() + fInactiveObjectDatas.size() + fActiveObjectDatas.size();
  }
  
  private void handleDeletionMarker(ObjectInfo info, Block block) {
    Set<ObjectInfo> infos = isActiveDeletionMarker(info, block) ? fActiveDeleteMarkers : fInactiveDeleteMarkers;
    infos.add(info);
  }

  private void handleObjectData(ObjectInfo info, Block block) {
    Set<ObjectInfo> infos = isCurrentBlock(info, block) ? fActiveObjectDatas : fInactiveObjectDatas;
    infos.add(info);
  }

  private boolean isActiveDeletionMarker(ObjectInfo info, Block block) {
    return info.getOldGenerationCount() != 0;
  }
  
  private boolean isCurrentBlock(ObjectInfo info, Block block) {
    return block.equals(fBlockRepo.getBlock(info));
  }

  private boolean isDeletionMarker(ObjectInfo info, Block block) {
    return info.isDeleted() && (fBlockRepo.getBlock(info).equals(block));
  }
  
}
