package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.block.*;
import org.memoriadb.util.MemoriaCRC32;

public class TransactionWriter implements IFileWriter {

  private final IMemoriaFile fFile;
  private final MaintenanceFreeBlockManager fBlockManager;
  private long fHeadRevision;
  
  public TransactionWriter(IMemoriaFile file, MaintenanceFreeBlockManager blockManager, long headRevision) {
    fFile = file;
    fBlockManager = blockManager;
    fHeadRevision = headRevision;
  }

  public void append(byte[] trxData) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);

    stream.write(BlockLayout.BLOCK_START_TAG);
    
    MemoriaCRC32 crc32 = new MemoriaCRC32();
    
    // revision + data.length + data + crc32
    long blockSize = 8 + 8 + trxData.length  + 8; 
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

    fFile.append(byteArrayOutputStream.toByteArray());
  }
  
  @Override
  public void close() {
    fFile.close();
  }

  @Override
  public IMemoriaFile getFile() {
    return fFile;
  }

  public void write(byte[] trxData) throws IOException {
    
    int blockSize = BlockLayout.getBlockSize(trxData.length);
    
    Block block = fBlockManager.getBlock(blockSize);
    
    if(block == null) {
      append(trxData);
    }
    else {
      freeBlock(block);
      write(block, trxData);
    }
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
