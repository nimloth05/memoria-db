package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.core.meta.MetaClass;
import org.memoriadb.util.CRC32Util;

public class FileReader {
  private final IFileReaderHandler fHandler;
  private final IMemoriaFile fFile;

  public FileReader(IMemoriaFile file, IFileReaderHandler handler) {
    
    fHandler = handler;
    fFile = file;
  }

  public void read() throws IOException {
    DataInputStream stream = new DataInputStream(fFile.getInputStream());

    // read file header
    long position = 0;
    while (stream.available() > 0) {
      position += readBlock(stream, position);
    }
    
    stream.close();
  }

  /**
   * @return Number of read bytes in this function
   * @throws IOException
   */
  private int readBlock(DataInputStream stream, long position) throws IOException {
    BlockTagUtil.assertTag(stream, BlockTagUtil.BLOCK_START_TAG);
    int blockSize = stream.readInt(); // the block size
    int transactionSize = stream.readInt(); // transactionsize

    byte[] transactionData = new byte[transactionSize];
    stream.read(transactionData);
    long crc32 = stream.readLong();
    
    // block may be bigger then the transaction-data -> skip 
    skip(stream, blockSize - transactionSize - (4 + 8)); // (transactionSize + crc32)

    boolean corrupt = true;
    if (CRC32Util.getChecksum(transactionData) == crc32) {
      readObjects(transactionData);
      corrupt = false;
    }

    fHandler.block(new Block(blockSize, position, corrupt));

    // startTag + size + data.length
    return BlockTagUtil.TAG_SIZE + 4 + blockSize;
  }

  private void readObject(byte[] data, int offset, int size) throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    long typeId = stream.readLong();
    long objectId = stream.readLong();
    long version = stream.readLong();

    if (MetaClass.isMetaClassObject(typeId)) {
      fHandler.metaClass(new HydratedObject(typeId, stream), objectId, version);
    }
    else {
      fHandler.object(new HydratedObject(typeId, stream), objectId, version);
    }
  }

  private void readObjects(byte[] data) throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));
    int offset = 0;

    while (stream.available() > 0) {
      int size = stream.readInt();
      readObject(data, offset + 4, size);
      offset += 4 + size;
      skip(stream, size);
    }
  }

  private void skip(DataInputStream stream, int size) throws IOException {
    if (stream.skip(size) != size) throw new RuntimeException("could not skip bytes: " + size);
  }

}
