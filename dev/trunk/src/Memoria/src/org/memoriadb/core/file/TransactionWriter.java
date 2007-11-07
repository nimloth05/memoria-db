package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.block.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.CRC32Util;

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
    
    // revision + data.length + data + crc32
    long blockSize = 8 + 8 + trxData.length  + 8; 
    stream.writeLong(blockSize);

    // transaction
    stream.writeLong(trxData.length);
    stream.writeLong(++fHeadRevision);
    stream.write(trxData);
    stream.writeLong(CRC32Util.getChecksum(trxData));

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

  public void write(byte[] trxData) {
    
    int blockSize = BlockLayout.getBlockSize(trxData.length);
    
    Block freeBlock = fBlockManager.getBlock(blockSize);
    
    try {
      append(trxData);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }
}
