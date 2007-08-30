package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.block.BlockLayout;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

public class FileWriter implements IFileWriter {

  private final IObjectRepo fObjectRepo;
  private final IMemoriaFile fFile;
  
  public FileWriter(IObjectRepo objectRepo, IMemoriaFile file) {
    super();
    fObjectRepo = objectRepo;
    fFile = file;
  }

  public void write(IdentityHashSet<Object> objects) {
    byte[] data = ObjectSerializer.serialize(fObjectRepo, objects);
    try {
      append(data);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  private void append(byte[] data) throws IOException {
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
}
