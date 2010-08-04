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

package org.memoriadb.core.file.write;

import java.io.IOException;
import java.util.*;

import org.memoriadb.OpenConfig;
import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.*;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.MemoriaCRC32;
import org.memoriadb.core.util.collection.CompoundIterator;
import org.memoriadb.core.util.io.MemoriaDataOutputStream;

public final class TransactionWriter {

  private final IMemoriaFile fFile;
  private final IBlockManager fBlockManager;
  private final IObjectRepository fRepo;
  private final BlockRepository fBlockRepo;
  private final OpenConfig fConfig;
  private final ICompressor fCompressor;
  private long fHeadRevision;

  public TransactionWriter(IObjectRepository repo, BlockRepository blockRepo, OpenConfig config, IMemoriaFile file, long headRevision, ICompressor compressor) {
    fConfig = config;
    if (repo == null) throw new IllegalArgumentException("objectRepo is null");
    if (config == null) throw new IllegalArgumentException("config is null");
    if (file == null) throw new IllegalArgumentException("file is null");

    fRepo = repo;
    fBlockRepo = blockRepo;
    fFile = file;
    fBlockManager = config.getBlockManager();
    fHeadRevision = headRevision;
    fCompressor = compressor;
  }

  public void close() {
    fFile.close();
  }

  public IBlockManager getBlockManager() {
    return fBlockManager;
  }

  public BlockRepository getBlockRepository() {
    return fBlockRepo;
  }

  public IMemoriaFile getFile() {
    return fFile;
  }

  public long getHeadRevision() {
    return fHeadRevision;
  }

  public IObjectRepository getRepo() {
    return fRepo;
  }

  public void write(Set<ObjectInfo> add, Set<ObjectInfo> update, Set<ObjectInfo> delete, IModeStrategy mode) throws Exception {
    write(add, update, delete, new HashSet<Block>(), mode);
  }

  private void addToTabooBlocks(Set<Block> tabooBlocks, ObjectInfo info) {
    Block block = fBlockRepo.getBlock(info);
    if (block == null) {
      // the info corresponds to a new object, so there is no taboo block for it
      return;
    }
    tabooBlocks.add(block);
  }

  private Block append(byte[] compressedTrx, Iterable<ObjectInfo> objectInfos) throws IOException {
    MemoriaDataOutputStream stream = new MemoriaDataOutputStream();

    stream.write(FileLayout.BLOCK_START_TAG);

    MemoriaCRC32 crc = new MemoriaCRC32();
    stream.writeLong(compressedTrx.length);
    crc.updateLong(compressedTrx.length);
    stream.writeLong(crc.getValue());
    
    stream.write(compressedTrx);
    crc = new MemoriaCRC32();
    crc.update(compressedTrx);
    stream.writeLong(crc.getValue());
    
    // first create the block...
    Block block = new Block(compressedTrx.length, fFile.getSize());
    
    block.addObjectIds(objectInfos);
    fBlockManager.add(block);

    fConfig.getListeners().triggerBeforeAppend(block);

    markAsLastWrittenBlock(block, FileLayout.WRITE_MODE_APPEND);
    // ... then add the data to the file.
    fFile.append(stream.toByteArray());
    
    // the block si written, synchronize.
    fFile.sync();

    fConfig.getListeners().triggerAfterAppend(block);

    return block;
  }

  /**
   * Saves the survivors of the given <tt>block</tt>;
   * @param block
   * @param block
   * @param tabooBlocks
   * @param tabooBlocks
   * @param mode
   * @param mode
   * @param decOGC Contains all objectInfos whose OldGenerationCount must be decremented.
   * 
   * @throws IOException
   */
  private void freeBlock(Block block, Set<Block> tabooBlocks, IModeStrategy mode, List<ObjectInfo> decOGC) throws Exception {
    
    SurvivorAgent survivorAgent = SurvivorAgent.create(fRepo, fBlockRepo, block);

    if (survivorAgent.hasSurvivors()) {
      saveSurvivors(tabooBlocks, mode, survivorAgent, decOGC);
    }

    // the following updates must be performed irrespective of the survivors.

    for (ObjectInfo info : survivorAgent.getInactiveObjectData()) {
      // silenty discard object-data

      decOGC.add(info);
    }
    
    for(ObjectInfo info : survivorAgent.getInactiveDeleteMarkers()) {
      fRepo.removeFromIndex(info);
      fBlockRepo.remove(info.getId());
    }
    
  }

  private boolean isDeletionMarkerWithoutOldGenerationObjectInfos(ObjectInfo info) {
    return info.getOldGenerationCount()==0 && info.isDeleted();
  }

  private void markAsLastWrittenBlock(Block block, int writeMode) throws IOException {
    HeaderHelper.updateBlockInfo(getFile(), block, writeMode);
  }

  /**
   * @param tabooBlocks
   * @param tabooBlocks
   * @param mode
   * @param mode
   * @param survivorAgent
   * @param survivorAgent
   * @param decOGC Contains all objectInfos whose OldGenerationCount must be decremented.
   * @throws java.io.IOException
   * @throws Exception
   */
  private void saveSurvivors(Set<Block> tabooBlocks, IModeStrategy mode, SurvivorAgent survivorAgent, List<ObjectInfo> decOGC) throws Exception, IOException {
    
    MemoriaDataOutputStream stream = new MemoriaDataOutputStream();
    long revision = writeTransaction(stream, survivorAgent.getActiveObjectData().size() + survivorAgent.getActiveDeleteMarkers().size());
    
    ObjectSerializer serializer = new ObjectSerializer(fRepo, mode, stream);

    writeAddOrUpdate(survivorAgent.getActiveObjectData(), tabooBlocks, serializer);
    writeDelete(survivorAgent.getActiveDeleteMarkers(), tabooBlocks, serializer);

    Block survivorsBlock = write(stream.toByteArray(), new CompoundIterator<ObjectInfo>(survivorAgent.getActiveObjectData(), survivorAgent.getActiveDeleteMarkers()), tabooBlocks, mode, decOGC);

    updateObjectInfo(revision, survivorsBlock, survivorAgent.getActiveObjectData());
    updateObjectInfo(revision, survivorsBlock, survivorAgent.getActiveDeleteMarkers());
    
  }

  private void setObjectInactive(ObjectInfo info, Block oldBlock) {
    if (oldBlock == null) throw new IllegalArgumentException("oldBlock is null");
    oldBlock.incrementInactiveObjectDataCount();
  }

  private void updateInfoAfterAdd(Set<ObjectInfo> infos, Block block, long revision) {
    for (ObjectInfo info : infos) {
      fBlockRepo.update(info, block);
      info.setRevision(revision);
    }
  }

  private void updateInfoAfterDelete(Set<ObjectInfo> infos, Block block, long revision) {
    for (ObjectInfo info : infos) {
      Block oldBlock = fBlockRepo.update(info, block);
      setObjectInactive(info, oldBlock);
      info.setRevision(revision);
      info.incrementOldGenerationCount();
    }
  }

  private void updateInfoAfterUpdate(Set<ObjectInfo> infos, Block block, long revision) {
    for (ObjectInfo info : infos) {
      Block oldBlock = fBlockRepo.update(info, block);
      setObjectInactive(info, oldBlock);
      info.setRevision(revision);
      info.incrementOldGenerationCount();
    }
  }

  /**
   * Updates the revision and block of the given {@link ObjectInfo}s 
   * @param revision
   * @param newBlock
   * @param activeObjectData
   */
  private void updateObjectInfo(long revision, Block newBlock, Iterable<ObjectInfo> activeObjectData) {
    for (ObjectInfo info : activeObjectData) {
      Block oldBlock = fBlockRepo.update(info, newBlock);
      setObjectInactive(info, oldBlock);
      info.setRevision(revision);
    }
  }

  private void write(Block block, byte[] trxData) throws IOException {
    MemoriaDataOutputStream stream = new MemoriaDataOutputStream();

    stream.write(trxData);
    byte[] garbage = new byte[(int)block.getBodySize()-trxData.length];
    stream.write(garbage);
    
    MemoriaCRC32 crc = new MemoriaCRC32();
    crc.update(trxData);
    crc.update(garbage);
    stream.writeLong(crc.getValue());
    
    fConfig.getListeners().triggerBeforeWrite(block);

    markAsLastWrittenBlock(block, FileLayout.WRITE_MODE_UPDATE);
    // dd the data after the BlockTag to the file
    fFile.write(stream.toByteArray(), block.getBodyStartPosition());

    // the block si written, synchronize.
    fFile.sync();
    
    fConfig.getListeners().triggerAfterWrite(block);
  }

  /**
   * Called recursively
   * @param trxData
   * @param trxData
   * @param objectDataCount
   * @param objectDataCount
   * @param tabooBlocks
   * @param tabooBlocks
   * @param mode
   * @param mode
   * @param decOGC Contains all objectInfos whose OldGenerationCount must be decremented.
   * @throws Exception
   * @return
   */
  private Block write(byte[] trxData, Iterable<ObjectInfo> objectInfos, Set<Block> tabooBlocks, IModeStrategy mode, List<ObjectInfo> decOGC) throws Exception {
    
    byte[] compressedTrx = fCompressor.compress(trxData);

    Block block = fBlockManager.allocatedRecyclebleBlock(compressedTrx.length, tabooBlocks);
    
    // no existing block matched the requirements of the Blockmanager, append the data in a new block.
    if (block == null) return append(compressedTrx, objectInfos);

    if(!block.isFree()) {
      freeBlock(block, tabooBlocks, mode, decOGC);
      // now all objects in the freed block must be inactive (inactive-ratio == 100%)
      if (block.getInactiveRatio() != 100) throw new MemoriaException("active objects in freed block: " + block);
    }

    block.resetBlock();
    block.addObjectIds(objectInfos);

    write(block, compressedTrx);
    return block;
  }

  private void write(Set<ObjectInfo> add, Set<ObjectInfo> update, Set<ObjectInfo> delete, Set<Block> tabooBlocks, IModeStrategy mode) throws Exception {
    
    MemoriaDataOutputStream stream = new MemoriaDataOutputStream();
    long revision = writeTransaction(stream, add.size() + update.size() + delete.size());
    
    ObjectSerializer serializer = new ObjectSerializer(fRepo, mode, stream);
    
    writeAddOrUpdate(add, tabooBlocks, serializer);
    writeAddOrUpdate(update, tabooBlocks, serializer);
    writeDelete(delete, tabooBlocks, serializer);

    List<ObjectInfo> decOGC = new ArrayList<ObjectInfo>();
    Block block = write(stream.toByteArray(), new CompoundIterator<ObjectInfo>(add, update, delete), tabooBlocks, mode, decOGC);
    
    updateInfoAfterAdd(add, block, revision);
    updateInfoAfterUpdate(update, block, revision);
    updateInfoAfterDelete(delete, block, revision);

    // must done at the end of the write-process to avoid abnormities.
    for(ObjectInfo info: decOGC) {
      info.decrementOldGenerationCount();
      
      if(isDeletionMarkerWithoutOldGenerationObjectInfos(info)) {
        fBlockRepo.getBlock(info).incrementInactiveObjectDataCount();
      }
      
    }
  }

  private void writeAddOrUpdate(Set<ObjectInfo> add, Set<Block> tabooBlocks, ObjectSerializer serializer) throws Exception {
    for (ObjectInfo info : add) {
      serializer.serialize(info);
      addToTabooBlocks(tabooBlocks, info);
    }
  }

  private void writeDelete(Set<ObjectInfo> delete, Set<Block> tabooBlocks, ObjectSerializer serializer) throws IOException {
    for (ObjectInfo info : delete) {
      if (!info.isDeleted()) throw new MemoriaException("trying to delete live object: " + info);
      serializer.markAsDeleted(info);
      addToTabooBlocks(tabooBlocks, info);
    }
  }

  private long writeTransaction(MemoriaDataOutputStream stream, long objectDataCount) throws IOException {
    // increment revision here. If it's the saving of survivors or an append,
    // either way, the revision must be incremeneted at the time of writing back
    stream.writeLong(++fHeadRevision);
    stream.writeLong(objectDataCount);
    return fHeadRevision;
  }
  
}
