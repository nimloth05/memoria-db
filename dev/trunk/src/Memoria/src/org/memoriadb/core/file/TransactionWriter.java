package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.block.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.CRC32Util;

public class TransactionWriter implements IFileWriter {

  private final IMemoriaFile fFile;
  private final BlockManager fBlockManager;
  
  public TransactionWriter(IMemoriaFile file, BlockManager blockManager) {
    fFile = file;
    fBlockManager = blockManager;
  }

  public void append(byte[] data) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);

    stream.write(BlockLayout.BLOCK_START_TAG);
    
    //  data.length + data + crc32
    long blockSize = 8 + data.length  + 8; 
    stream.writeLong(blockSize);

    // transaction
    stream.writeLong(data.length);
    stream.write(data);
    stream.writeLong(CRC32Util.getChecksum(data));

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

  public void write(byte[] data) {
    try {
      append(data);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }
}
