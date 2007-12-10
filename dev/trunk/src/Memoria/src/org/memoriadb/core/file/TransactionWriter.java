package org.memoriadb.core.file;

import java.io.*;
import java.util.*;

import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.SurvivorAgent;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.MemoriaCRC32;

public final class TransactionWriter implements ITransactionWriter {
 
  private final IMemoriaFile fFile;
  private final IBlockManager fBlockManager;
  private long fHeadRevision;
  private final IObjectRepository fRepo;
  private final OpenConfig fConfig;

  public TransactionWriter(IObjectRepository repo, OpenConfig config, IMemoriaFile file, long headRevision) {
    fConfig = config;
    if (repo == null) throw new IllegalArgumentException("objectRepo is null");
    if (config == null) throw new IllegalArgumentException("config is null");
    if(file == null) throw new IllegalArgumentException("file is null");
    
    fRepo = repo;
    fFile = file;
    fBlockManager = config.getBlockManager();
    fHeadRevision = headRevision;
  }

  @Override
  public void close() {
    fFile.close();
  }
  
  @Override
  public IBlockManager getBlockManager() {
    return fBlockManager;
  }

  @Override
  public IMemoriaFile getFile() {
    return fFile;
  }

  @Override
  public long getHeadRevision() {
    return fHeadRevision;
  }

  @Override
  public IObjectRepository getRepo() {
    return fRepo;
  }

  @Override
  public void write(Set<ObjectInfo> add, Set<ObjectInfo> update, Set<ObjectInfo> delete, IModeStrategy mode) throws Exception {
    write(add, update, delete, new HashSet<Block>(), mode);
  }

  private Block append(byte[] trxData, int numberOfObjects) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);

    stream.write(FileLayout.BLOCK_START_TAG);

    MemoriaCRC32 crc = new MemoriaCRC32();
    long blockSize = FileLayout.TRX_OVERHEAD + trxData.length;
    stream.writeLong(blockSize);
    crc.updateLong(blockSize);
    stream.writeLong(crc.getValue());
    
    // transaction
    writeTransaction(trxData, stream);

    // first create the block...
    Block block = new Block(blockSize, fFile.getSize());
    block.setNumberOfObjectData(numberOfObjects);
    fBlockManager.add(block);
    
    System.out.println("append " + numberOfObjects);
     
    fConfig.getListeners().triggerBeforeAppend(block);
    
    markAsLastWrittenBlock(block, FileLayout.WRITE_MODE_APPEND);
    // ... then add the data to the file.
    fFile.append(byteArrayOutputStream.toByteArray());
    
    fConfig.getListeners().triggerAfterAppend(block);

    return block;
  }
  
  /**
   * Safes the survivors of the given <tt>block</tt>;
   * 
   * @throws IOException
   */
  private void freeBlock(Block block, Set<Block> tabooBlocks, IModeStrategy mode) throws Exception {
    SurvivorAgent survivorAgent = new SurvivorAgent(fRepo, fFile, block);
    if(survivorAgent.hasNoSurvivors()) return;
    
    // save survivors recursively
    write(new HashSet<ObjectInfo>(), survivorAgent.getUpdates(), survivorAgent.getDeleteMarkers(), tabooBlocks, mode);
  }

  private void markAsLastWrittenBlock(Block block, int writeMode) throws IOException {
    HeaderHelper.updateBlockInfo(getFile(), block, writeMode);
  }

  private void updateInfoForAdd(Set<ObjectInfo> infos, Block block) {
    for(ObjectInfo info: infos){
      info.changeCurrentBlock(block);
      info.setRevision(fHeadRevision);
    }
  }

  private void updateInfoForDelete(Set<ObjectInfo> infos, Block block) {
    for(ObjectInfo info: infos){
      info.changeCurrentBlock(block);
      info.setRevision(fHeadRevision);
      info.incrememntOldGenerationCount();
    }
  }

  private void updateInfoForUpdate(Set<ObjectInfo> infos, Block block) {
    for(ObjectInfo info: infos){
      info.changeCurrentBlock(block);
      info.setRevision(fHeadRevision);
      info.incrememntOldGenerationCount();
    }
  }

  private void write(Block block, byte[] trxData) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);

    // transaction
    writeTransaction(trxData, stream);
    
    fConfig.getListeners().triggerBeforeWrite(block);
    
    markAsLastWrittenBlock(block, FileLayout.WRITE_MODE_UPDATE);
    // dd the data after the BlockTag to the file
    fFile.write(byteArrayOutputStream.toByteArray(), block.getBodyStartPosition());
    
    fConfig.getListeners().triggerAfterWrite(block);
  } 

  private Block write(byte[] trxData, int numberOfObjects, Set<Block> tabooBlocks, IModeStrategy mode) throws Exception {
    int blockSize = FileLayout.getBlockSize(trxData.length);

    Block block = fBlockManager.allocatedRecyclebleBlock(blockSize, tabooBlocks);
    
    // no existing block matched the requirements of the Blockmanager, append the data in a new block.
    if (block == null) return append(trxData, numberOfObjects);

    freeBlock(block, tabooBlocks, mode);
    
    // now all objects in the freed block must be inactive (inactive-ratio == 100%)
    if(block.getInactiveRatio() != 100) throw new MemoriaException("active objects in freed block: " + block);
    block.resetBlock(numberOfObjects);
    
    System.out.println("write " + numberOfObjects);
    
    write(block, trxData);
    return block;    
  }

  /**
   * Called recursively
   * @throws Exception 
   */
  private void write(Set<ObjectInfo> add, Set<ObjectInfo> update, Set<ObjectInfo> delete, Set<Block> tabooBlocks, IModeStrategy mode) throws Exception {
    
    ObjectSerializer serializer = new ObjectSerializer(fRepo, mode);
    
    writeAddOrUpdate(add, tabooBlocks, serializer);
    writeAddOrUpdate(update, tabooBlocks, serializer);
    writeDelete(delete, tabooBlocks, serializer);
    Block block = write(serializer.getBytes(), add.size() + update.size() + delete.size(), tabooBlocks, mode);

    updateInfoForAdd(add, block);
    updateInfoForUpdate(update, block);
    updateInfoForDelete(delete, block);
  }

  private void writeAddOrUpdate(Set<ObjectInfo> add, Set<Block> tabooBlocks, ObjectSerializer serializer) throws Exception {
    for(ObjectInfo info: add) {
      serializer.serialize(info);
      tabooBlocks.add(info.getCurrentBlock());
    }
  }

  private void writeDelete(Set<ObjectInfo> delete, Set<Block> tabooBlocks, ObjectSerializer serializer) throws IOException {
    for(ObjectInfo info: delete){
      if(!info.isDeleted()) throw new MemoriaException("trying to delete live object: " + info);
      serializer.markAsDeleted(info);
      tabooBlocks.add(info.getCurrentBlock());
    }
  }

  private void writeTransaction(byte[] trxData, DataOutputStream stream) throws IOException {
    MemoriaCRC32 crc = new MemoriaCRC32();
    
    stream.writeLong(trxData.length);
    crc.updateLong(trxData.length);

    // increment revision here. If it's the saving of survivors or an append,
    // either way, the revision must be incremeneted at the time of writing back
    stream.writeLong(++fHeadRevision);
    crc.updateLong(fHeadRevision);

    stream.write(trxData);
    crc.update(trxData);

    stream.writeLong(crc.getValue());
  }
}
