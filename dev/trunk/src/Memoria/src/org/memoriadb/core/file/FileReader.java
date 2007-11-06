package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.block.*;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.FileCorruptException;
import org.memoriadb.util.*;

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
  private long readBlock(DataInputStream stream, long position) throws IOException {
    BlockLayout.assertBlockTag(stream);
    long blockSize = stream.readLong(); // the block size
    long transactionSize = stream.readLong(); // transactionsize

    byte[] transactionData = new byte[(int) transactionSize];
    stream.read(transactionData);
    long crc32 = stream.readLong();

    // block may be bigger then the transaction-data -> skip
    skip(stream, blockSize - transactionSize - (8 + 8)); // (transactionSize + crc32)

    if (CRC32Util.getChecksum(transactionData) != crc32) throw new FileCorruptException("wrong checksum for block at position " + position);

    readObjects(transactionData);
    fHandler.block(new Block(blockSize, position));

    // startTag + blockSize dataSize + data.length
    return BlockLayout.TAG_SIZE + 8 + 8 + blockSize;
  }

  private void readObject(byte[] data, int offset, int size) throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    long typeId = stream.readLong();
    long objectId = stream.readLong();
    long version = stream.readLong();
    
    if(typeId == IdConstants.OBJECT_DELETED){
      fHandler.objectDeleted(objectId, version);
      return;
    }
    else if (typeId == IdConstants.METACLASS_DELETED) {
      fHandler.memoriaClassDeleted(objectId, version);
      return;
    }

    // no deleteMarker encountered
    if (MemoriaFieldClass.isMetaClassObject(typeId)) {
      fHandler.memoriaClass(new HydratedObject(typeId, stream), objectId, version);
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

  private void skip(DataInputStream stream, long size) throws IOException {
    if (stream.skip(size) != size) throw new RuntimeException("could not skip bytes: " + size);
  }

}
