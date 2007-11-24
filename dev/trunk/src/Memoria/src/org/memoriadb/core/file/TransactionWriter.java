package org.memoriadb.core.file;

import java.io.*;
import java.util.*;

import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.util.MemoriaCRC32;

public final class TransactionWriter implements ITransactionWriter {
 
  private final IMemoriaFile fFile;
  private final IBlockManager fBlockManager;
  private long fHeadRevision;
  private final IObjectRepository fRepo;
  private final SurvivorAgent fSurvivorAgent;
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
    fSurvivorAgent = new SurvivorAgent(repo, file);
  }

  public Block append(byte[] trxData, int numberOfObjects) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);

    stream.write(FileLayout.BLOCK_START_TAG);

    MemoriaCRC32 crc32 = new MemoriaCRC32();

    // data.length + revision + data + crc32
    long blockSize = FileLayout.TRX_OVERHEAD + trxData.length;
    stream.writeLong(blockSize);
    crc32.updateLong(blockSize);

    // transaction
    writeTransaction(trxData, stream, crc32);

    // first create the block...
    Block block = new Block(blockSize, fFile.getSize());
    block.setNumberOfObjectData(numberOfObjects);
    fBlockManager.add(block);
     
    fConfig.getListeners().triggerBeforeAppend(block);
    
    markAsLastWrittenBlock(block, FileLayout.WRITE_MODE_APPEND);
    // ... then add the data to the file.
    fFile.append(byteArrayOutputStream.toByteArray());
    
    fConfig.getListeners().triggerAfterAppend(block);

    return block;
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
  public void write(Set<ObjectInfo> add, Set<ObjectInfo> update, Set<ObjectInfo> delete) throws IOException {
    write(add, update, delete, new HashSet<Block>());
  }
  
  /**
   * Called recursively
   */
  public void write(Set<ObjectInfo> add, Set<ObjectInfo> update, Set<ObjectInfo> delete, Set<Block> tabooBlocks) throws IOException {
    
    ObjectSerializer serializer = new ObjectSerializer(fRepo);
    
    writeAddOrUpdate(add, tabooBlocks, serializer);
    writeAddOrUpdate(update, tabooBlocks, serializer);
    writeDelete(delete, tabooBlocks, serializer);
    Block block = write(serializer.getBytes(), add.size() + update.size() + delete.size(), tabooBlocks);

    updateInfoForAdd(add, block);
    updateInfoForUpdate(update, block);
    updateInfoForDelete(delete, block);
  }

  /**
   * Safes the survivors of the given <tt>block</tt>;
   * 
   * @throws IOException
   */
  private void freeBlock(Block block, Set<Block> tabooBlocks) throws IOException {
    Set<ObjectInfo> survivors = fSurvivorAgent.getSurvivors(block);
    if(survivors.isEmpty()) return;
    
    // save survivors recursively
    write(new HashSet<ObjectInfo>(), survivors, new HashSet<ObjectInfo>(), tabooBlocks);
  }

  private void markAsLastWrittenBlock(Block block, int writeMode) throws IOException {
    FileHeaderHelper.updateBlockInfo(getFile(), block, writeMode);
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

    MemoriaCRC32 crc32 = new MemoriaCRC32();

    // first entry in the checked area is the size of the block, which is not overwritten.
    crc32.updateLong(block.getSize());

    // transaction
    writeTransaction(trxData, stream, crc32);
    
    fConfig.getListeners().triggerBeforeWrite(block);
    
    markAsLastWrittenBlock(block, FileLayout.WRITE_MODE_UPDATE);
    // dd the data after the BlockTag to the file
    fFile.write(byteArrayOutputStream.toByteArray(), block.getPosition() + FileLayout.BLOCK_OVERHEAD);
    
    fConfig.getListeners().triggerAfterWrite(block);
  }

  private Block write(byte[] trxData, int numberOfObjects, Set<Block> tabooBlocks) throws IOException {
    int blockSize = FileLayout.getBlockSize(trxData.length);

    // this call may return to this TransactionWriter recursivley
    Block block = fBlockManager.allocatedRecyclebleBlock(blockSize, tabooBlocks);
    
    // no existing block matched the requirements of the Blockmanager, append the data in a new block.
    if (block == null) return append(trxData, numberOfObjects);

    freeBlock(block, tabooBlocks);
    
    write(block, trxData);
    return block;    
  }

  private void writeAddOrUpdate(Set<ObjectInfo> add, Set<Block> tabooBlocks, ObjectSerializer serializer) {
    for(ObjectInfo info: add) {
      serializer.serialize(info);
      tabooBlocks.add(info.getCurrentBlock());
    }
  }

  private void writeDelete(Set<ObjectInfo> delete, Set<Block> tabooBlocks, ObjectSerializer serializer) {
    for(ObjectInfo info: delete){
      serializer.markAsDeleted(info);
      tabooBlocks.add(info.getCurrentBlock());
    }
  }

  private void writeTransaction(byte[] trxData, DataOutputStream stream, MemoriaCRC32 crc32) throws IOException {
    stream.writeLong(trxData.length);
    crc32.updateLong(trxData.length);

    // increment revision here. If it's a saving of survivors or an append,
    // either way the revision must be incremeneted at the time of writing back
    stream.writeLong(++fHeadRevision);
    crc32.updateLong(fHeadRevision);

    stream.write(trxData);
    crc32.update(trxData);

    stream.writeLong(crc32.getValue());
  }
}
