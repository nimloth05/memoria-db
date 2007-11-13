package org.memoriadb.core.file;

import java.io.*;
import java.util.*;

import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.util.MemoriaCRC32;

public class TransactionWriter implements ITransactionWriter {
 
  private final IMemoriaFile fFile;
  private final IBlockManager fBlockManager;
  private long fHeadRevision;
  private final IObjectRepo fRepo;
  private final DBMode fMode;
  private final SurvivorAgent fSurvivorAgent;

  public TransactionWriter(IObjectRepo repo, IBlockManager blockManager, IMemoriaFile file, long headRevision, DBMode mode) {
    if (repo == null) throw new IllegalArgumentException("objectRepo is null");
    if (mode == null) throw new IllegalArgumentException("dbMode is null");
    if(file == null) throw new IllegalArgumentException("file is null");
    if(blockManager == null) throw new IllegalArgumentException("blockManager is null");
    
    fRepo = repo;
    fFile = file;
    fBlockManager = blockManager;
    fHeadRevision = headRevision;
    fMode = mode;
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

    // ... then add the data to the file.
    fFile.append(byteArrayOutputStream.toByteArray());

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
  public DBMode getMode() {
    return fMode;
  }

  @Override
  public IObjectRepo getRepo() {
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
    
    ObjectSerializer serializer = new ObjectSerializer(fRepo, fMode);
    
    for(ObjectInfo info: add) {
      serializer.serialize(info.getObj());
      tabooBlocks.add(info.getCurrentBlock());
    }
    for(ObjectInfo info: update) {
      serializer.serialize(info.getObj());
      tabooBlocks.add(info.getCurrentBlock());
    }
    for(ObjectInfo info: delete){
      serializer.markAsDeleted(info.getId());
      tabooBlocks.add(info.getCurrentBlock());
    }
    Block block = write(serializer.getBytes(), add.size() + update.size() + delete.size(), tabooBlocks);

    updateInfoForAdd(add, block);
    updateInfoForUpdate(update, block);
    updateInfoForDelete(delete, block);
  }

  /**
   * Safes the survivors of the given <tt>block</tt>;
   * @throws IOException 
   */
  private void freeBlock(Block block, Set<Block> tabooBlocks) throws IOException {
    Set<ObjectInfo> survivors = fSurvivorAgent.getSurvivors(block);
    if(survivors.isEmpty()) return;
    
    // save survivors recursively
    write(new HashSet<ObjectInfo>(), survivors, new HashSet<ObjectInfo>(), tabooBlocks);
            
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

    // dd the data after the BlockTag to the file 
    fFile.write(byteArrayOutputStream.toByteArray(), block.getPosition() + FileLayout.BLOCK_OVERHEAD);
  }

  private Block write(byte[] trxData, int numberOfObjects, Set<Block> tabooBlocks) throws IOException {
    int blockSize = FileLayout.getBlockSize(trxData.length);

    // this call may return to this TransactionWriter recursivley
    Block block = fBlockManager.findRecyclebleBlock(blockSize, tabooBlocks);
    
    
    // no existing block matched the requirements of the Blockmanager, append the data in a new block.
    if (block == null) return append(trxData, numberOfObjects);

    freeBlock(block, tabooBlocks);
    
    write(block, trxData);
    return block;    
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
