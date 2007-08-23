package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.block.BlockTagUtil;
import org.memoriadb.core.repo.IObjectRepo;
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

  void append(byte[] data) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream file = new DataOutputStream(byteArrayOutputStream);

    file.write(BlockTagUtil.BLOCK_START_TAG);
    
    // blockSize + data.length + crc32
    int blockSize = 4 + data.length  + 8; 
    file.writeInt(blockSize);

    // transaction
    file.writeInt(data.length);
    file.write(data);
    file.writeLong(CRC32Util.getChecksum(data));

    fFile.append(byteArrayOutputStream.toByteArray());
  }
}
