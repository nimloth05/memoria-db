package org.memoriadb.core.file.write;

import java.io.*;
import java.util.*;

import org.memoriadb.OpenConfig;
import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.SurvivorAgent;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.*;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.MemoriaCRC32;
import org.memoriadb.core.util.io.*;

public final class TransactionWriter {

  private final IMemoriaFile fFile;
  private final IBlockManager fBlockManager;
  private long fHeadRevision;
  private final IObjectRepository fRepo;
  private final OpenConfig fConfig;
  private final ICompressor fCompressor;

  public TransactionWriter(IObjectRepository repo, OpenConfig config, IMemoriaFile file, long headRevision, ICompressor compressor) {
    fConfig = config;
    if (repo == null) throw new IllegalArgumentException("objectRepo is null");
    if (config == null) throw new IllegalArgumentException("config is null");
    if (file == null) throw new IllegalArgumentException("file is null");

    fRepo = repo;
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

  private Block append(byte[] compressedTrx, long objectDataCount) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    MemoriaDataOutputStream stream = new MemoriaDataOutputStream(byteArrayOutputStream);

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
    
    block.setObjectDataCount(objectDataCount);
    fBlockManager.add(block);

    fConfig.getListeners().triggerBeforeAppend(block);

    markAsLastWrittenBlock(block, FileLayout.WRITE_MODE_APPEND);
    // ... then add the data to the file.
    fFile.append(byteArrayOutputStream.toByteArray());

    fConfig.getListeners().triggerAfterAppend(block);

    return block;
  }

  /**
   * Saves the survivors of the given <tt>block</tt>;
   * @param decOGC Contains all objectInfos whose OldGenerationCount must be decremented.
   * 
   * @throws IOException
   */
  private void freeBlock(Block block, Set<Block> tabooBlocks, IModeStrategy mode, List<ObjectInfo> decOGC) throws Exception {
    SurvivorAgent survivorAgent = new SurvivorAgent(fRepo, fFile, block, fCompressor);

    if (survivorAgent.hasSurvivors()) {
      saveSurvivors(tabooBlocks, mode, survivorAgent, decOGC);
    }

    // the following updates must be performed irrespective of the survivors.

    for (ObjectInfo info : survivorAgent.getInactiveObjectData()) {
      // silenty discard object-data

      decOGC.add(info);
    }

    // silently discard inactive delete-markers
  }

  private void markAsLastWrittenBlock(Block block, int writeMode) throws IOException {
    HeaderHelper.updateBlockInfo(getFile(), block, writeMode);
  }

  /**
   * @param decOGC Contains all objectInfos whose OldGenerationCount must be decremented.
   */
  private void saveSurvivors(Set<Block> tabooBlocks, IModeStrategy mode, SurvivorAgent survivorAgent, List<ObjectInfo> decOGC) throws Exception, IOException {
    
    MemoriaByteArrayOutputStream stream = new MemoriaByteArrayOutputStream();
    long revision = writeTransaction(stream, survivorAgent.getActiveObjectData().size() + survivorAgent.getActiveDeleteMarkers().size());
    
    ObjectSerializer serializer = new ObjectSerializer(fRepo, mode, stream);

    writeAddOrUpdate(survivorAgent.getActiveObjectData(), tabooBlocks, serializer);
    writeDelete(survivorAgent.getActiveDeleteMarkers(), tabooBlocks, serializer);

    Block survivorsBlock = write(stream.toByteArray(), survivorAgent.getActiveObjectData().size() + survivorAgent.getActiveDeleteMarkers().size(), tabooBlocks, mode, decOGC);

    for (ObjectInfo info : survivorAgent.getActiveObjectData()) {
      info.changeCurrentBlock(survivorsBlock);
      info.setRevision(revision);
    }

    for (ObjectInfo info : survivorAgent.getActiveDeleteMarkers()) {
      info.changeCurrentBlock(survivorsBlock);
      info.setRevision(revision);
    }
  }

  private void updateInfoAfterAdd(Set<ObjectInfo> infos, Block block, long revision) {
    for (ObjectInfo info : infos) {
      info.changeCurrentBlock(block);
      info.setRevision(revision);
    }
  }

  private void updateInfoAfterDelete(Set<ObjectInfo> infos, Block block, long revision) {
    for (ObjectInfo info : infos) {
      info.changeCurrentBlock(block);
      info.setRevision(revision);
      info.incrementOldGenerationCount();
      info.setDeleteMarkerPersistent();
    }
  }

  private void updateInfoAfterUpdate(Set<ObjectInfo> infos, Block block, long revision) {
    for (ObjectInfo info : infos) {
      info.changeCurrentBlock(block);
      info.setRevision(revision);
      info.incrementOldGenerationCount();
    }
  }

  private void write(Block block, byte[] trxData, long objectDataCount) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    MemoriaDataOutputStream stream = new MemoriaDataOutputStream(byteArrayOutputStream);

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
    fFile.write(byteArrayOutputStream.toByteArray(), block.getBodyStartPosition());

    fConfig.getListeners().triggerAfterWrite(block);
  }

  /**
   * Called recursively
   * @param decOGC Contains all objectInfos whose OldGenerationCount must be decremented. 
   */
  private Block write(byte[] trxData, long objectDataCount, Set<Block> tabooBlocks, IModeStrategy mode, List<ObjectInfo> decOGC) throws Exception {
    
    byte[] compressedTrx = fCompressor.compress(trxData);

    Block block = fBlockManager.allocatedRecyclebleBlock(compressedTrx.length, tabooBlocks);
    
    // no existing block matched the requirements of the Blockmanager, append the data in a new block.
    if (block == null) return append(compressedTrx, objectDataCount);

    freeBlock(block, tabooBlocks, mode, decOGC);

    // now all objects in the freed block must be inactive (inactive-ratio == 100%)
    if (block.getInactiveRatio() != 100) throw new MemoriaException("active objects in freed block: " + block);
    block.resetBlock(objectDataCount);

    write(block, compressedTrx, objectDataCount);
    return block;
  }

  private void write(Set<ObjectInfo> add, Set<ObjectInfo> update, Set<ObjectInfo> delete, Set<Block> tabooBlocks, IModeStrategy mode) throws Exception {
    
    MemoriaByteArrayOutputStream stream = new MemoriaByteArrayOutputStream();
    long revision = writeTransaction(stream, add.size() + update.size() + delete.size());
    
    ObjectSerializer serializer = new ObjectSerializer(fRepo, mode, stream);
    
    writeAddOrUpdate(add, tabooBlocks, serializer);
    writeAddOrUpdate(update, tabooBlocks, serializer);
    writeDelete(delete, tabooBlocks, serializer);

    List<ObjectInfo> decOGC = new ArrayList<ObjectInfo>();
    Block block = write(stream.toByteArray(), add.size() + update.size() + delete.size(), tabooBlocks, mode, decOGC);
    
    updateInfoAfterAdd(add, block, revision);
    updateInfoAfterUpdate(update, block, revision);
    updateInfoAfterDelete(delete, block, revision);

    // must done at the end of the write-process to avoid abnormities.
    for(ObjectInfo info: decOGC) {
      info.decrementOldGenerationCount();
    }
  }

  private void writeAddOrUpdate(Set<ObjectInfo> add, Set<Block> tabooBlocks, ObjectSerializer serializer) throws Exception {
    for (IObjectInfo info : add) {
      serializer.serialize(info);
      tabooBlocks.add(info.getCurrentBlock());
    }
  }

  private void writeDelete(Set<ObjectInfo> delete, Set<Block> tabooBlocks, ObjectSerializer serializer) throws IOException {
    for (IObjectInfo info : delete) {
      if (!info.isDeleted()) throw new MemoriaException("trying to delete live object: " + info);
      serializer.markAsDeleted(info);
      tabooBlocks.add(info.getCurrentBlock());
    }
  }

  private long writeTransaction(MemoriaByteArrayOutputStream stream, long objectDataCount) throws IOException {
    // increment revision here. If it's the saving of survivors or an append,
    // either way, the revision must be incremeneted at the time of writing back
    stream.writeLong(++fHeadRevision);
    stream.writeLong(objectDataCount);
    return fHeadRevision;
  }
}
