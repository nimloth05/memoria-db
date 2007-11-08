package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.block.*;
import org.memoriadb.util.MemoriaCRC32;

public class TransactionWriter implements ITransactionWriter {

  private final IMemoriaFile fFile;
  private final MaintenanceFreeBlockManager fBlockManager;
  private long fHeadRevision;

  public TransactionWriter(IMemoriaFile file, MaintenanceFreeBlockManager blockManager, long headRevision) {
    fFile = file;
    fBlockManager = blockManager;
    fHeadRevision = headRevision;
  }

  public Block append(byte[] trxData, int numberOfObjects) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);

    stream.write(FileLayout.BLOCK_START_TAG);

    MemoriaCRC32 crc32 = new MemoriaCRC32();

    // data.length + revision + data + crc32
    long blockSize = 8 + 8 + trxData.length + 8;
    stream.writeLong(blockSize);
    crc32.updateLong(blockSize);

    // transaction
    stream.writeLong(trxData.length);
    crc32.updateLong(trxData.length);

    stream.writeLong(++fHeadRevision);
    crc32.updateLong(fHeadRevision);

    stream.write(trxData);
    crc32.update(trxData);

    stream.writeLong(crc32.getValue());

    // first create the block...
    Block block = new Block(blockSize, fFile.getSize(), numberOfObjects);
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

  public Block write(byte[] trxData, int numberOfObjects) throws IOException {

    int blockSize = FileLayout.getBlockSize(trxData.length);

    Block block = fBlockManager.findRecyclebleBlock(blockSize);
    
    // no existing block matched the requirements of the Blockmanager, append the data in a new block.
    if (block == null) return append(trxData, numberOfObjects);

    freeBlock(block);
    write(block, trxData);
    return block;
  }

  /**
   * Safes the survivors of the given <tt>block</tt>;
   */
  private void freeBlock(Block block) {

  }

  private void write(Block block, byte[] trxData) {
  // TODO Auto-generated method stub

  }
}
