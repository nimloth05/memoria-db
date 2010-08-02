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

package org.memoriadb.core.block;

import org.memoriadb.block.Block;
import org.memoriadb.core.IObjectRepository;
import org.memoriadb.core.ObjectInfo;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.ICompressor;
import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.read.BlockReader;
import org.memoriadb.core.file.read.HydratedObject;
import org.memoriadb.core.file.read.IFileReaderHandler;
import org.memoriadb.core.util.collection.identity.IdentityHashSet;
import org.memoriadb.core.util.io.IOUtil;
import org.memoriadb.core.util.io.MemoriaDataInputStream;
import org.memoriadb.id.IObjectId;

import java.io.IOException;
import java.util.Set;

/**
 * Computes the survivors in a block.
 * 
 * @author msc
 */
public class SurvivorAgent implements IFileReaderHandler  {
  
  // use IdentityHashSet for better performance
  private final Set<ObjectInfo> fActiveObjectData = IdentityHashSet.create();
  private final Set<ObjectInfo> fActiveDeleteMarkers =  IdentityHashSet.create();
  private final Set<ObjectInfo> fInactiveObjectDatas = IdentityHashSet.create();
  private final Set<ObjectInfo> fInactiveDeleteMarkers = IdentityHashSet.create();
  
  private final IObjectRepository fRepo;
  private final IMemoriaFile fFile;
  private final ICompressor fCompressor;
  
  public SurvivorAgent(IObjectRepository repo, IMemoriaFile file, Block block, ICompressor compressor) {
    fFile = file;
    fRepo = repo;
    fCompressor = compressor;
    computeSurvivors(block);
  }
   
  @Override
  public void block(Block block) {}

  public void computeSurvivors(Block block) {
    MemoriaDataInputStream stream = new MemoriaDataInputStream(fFile.getInputStream(block.getPosition()));
    BlockReader reader = new BlockReader(fCompressor);
    
    try {
      reader.readBlock(stream, new Block(1), fRepo.getIdFactory(), this, new AlwaysThrowErrorHandler());
    }
    catch (IOException e) { 
      throw new MemoriaException(e);
    }
    finally {
      IOUtil.close(stream);
    }
    
    if(block.getObjectDataCount() != getObjectDataCount()) throw new MemoriaException("objectDataCount mismatch. File: " + getObjectDataCount() + " in memory: " + block.getObjectDataCount());
    if(block.getInactiveObjectDataCount() != getInactiveObjectDataCount()) throw new MemoriaException("inactiveObjectDataCount mismatch. File: " + getInactiveObjectDataCount() + " in memory: " + block.getInactiveObjectDataCount());
  }

  public Set<ObjectInfo> getActiveDeleteMarkers() {
    return fActiveDeleteMarkers;
  }
  
  public Set<ObjectInfo> getActiveObjectData() {
    return fActiveObjectData;
  }
  
  public Set<ObjectInfo> getInactiveDeleteMarkers() {
    return fInactiveDeleteMarkers;
  }

  public Set<ObjectInfo> getInactiveObjectData() {
    return fInactiveObjectDatas;
  }

  public boolean hasSurvivors() {
    return !fActiveObjectData.isEmpty() || !fActiveDeleteMarkers.isEmpty();
  }

  @Override
  public void memoriaClass(HydratedObject metaClass, IObjectId id, long revision, int size) {
    handleUpdate(id, revision);
  }
  
  @Override
  public void memoriaClassDeleted(IObjectId id, long revision) {
    handleDelete(id, revision);
  }

  @Override
  public void object(HydratedObject object, IObjectId id, long revision, int size) {
    handleUpdate(id, revision);
  }

  @Override
  public void objectDeleted(IObjectId id, long revision) {
    handleDelete(id, revision);
  }

  private int getInactiveObjectDataCount() {
    return fInactiveDeleteMarkers.size() + fInactiveObjectDatas.size();
  }

  private int getObjectDataCount() {
    return fActiveDeleteMarkers.size() + fInactiveDeleteMarkers.size() + fInactiveObjectDatas.size() + fActiveObjectData.size();
  }
  
  private void handleDelete(IObjectId id, long revision) {
    ObjectInfo info = fRepo.getObjectInfoForId(id);
    long revisionFromObjectRepo = info.getRevision();
    if(revision != revisionFromObjectRepo) throw new MemoriaException("Wrong revision for DeletionMarker in repo: " + revisionFromObjectRepo + " expected " + revision);
    
    // check if deletionMarker must be saved.
    if(info.getOldGenerationCount() == 0) {
      // the DeletionMarker is not used anymore...
      fInactiveDeleteMarkers.add(info);
    }
    else {
      fActiveDeleteMarkers.add(info);
    }
  }

  private void handleUpdate(IObjectId id, long revision) {
    ObjectInfo info = fRepo.getObjectInfoForId(id);
    long revisionFromObjectRepo = info.getRevision();
    if(revision > revisionFromObjectRepo) throw new MemoriaException("Wrong revision for Object in repo: " + revisionFromObjectRepo + " expected " + revision);
    
    if(revisionFromObjectRepo == revision){
      fActiveObjectData.add(info);
    }
    else {
      // return this object to adjust the oldGenerationCount
      fInactiveObjectDatas.add(info);
    }
  }
  
  
  
}
