package org.memoriadb.core.file;

import java.io.*;
import java.util.*;

import org.memoriadb.Memoria;
import org.memoriadb.block.Block;
import org.memoriadb.core.CreateConfig;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.*;

public class HeaderHelper {

  public static final long NO_CURRENT_BLOCK = -1;
  
  public static Header getHeader(DataInputStream stream) throws IOException {
    checkMemoriaTag(stream);

    LastWrittenBlockInfo readCurrentBlockInfo = readLastWrittenBlockInfo(stream);

    int headerInfoSize = stream.readInt();

    byte[] headerInfo = new byte[headerInfoSize];
    stream.readFully(headerInfo);
    
    long readCrc = stream.readLong();
    
    MemoriaCRC32 crc = new MemoriaCRC32();
    crc.update(headerInfoSize);
    crc.update(headerInfo);
    if(readCrc != crc.getValue()) throw new MemoriaException("header corrupt");
    
    return readHeaderInfo(readCurrentBlockInfo, headerInfoSize, headerInfo);
  }

  /**
   * Stores the position of the last updated block in the file-header. 
   * @param writeMode {@link FileLayout#WRITE_MODE_APPEND} or {@link FileLayout#WRITE_MODE_UPDATE}
   */
  public static void updateBlockInfo(IMemoriaFile file, Block block, int writeMode) throws IOException {
    byte[] data = getCurrentBlockInfo(block.getPosition(), writeMode);
    
    file.write(data, FileLayout.CURRENT_BLOCK_INFO_START_POSITION);
  }

  public static void writeHeader(IMemoriaFile file, CreateConfig config) throws IOException {
    writeMemoriaTag(file);
    
    writeDefaultLastWrittenBlockInfo(file);

    byte[] headerInfo = writeHeaderInfo(config);
    int headerInfoSize = headerInfo.length;
    
    MemoriaCRC32 crc = new MemoriaCRC32();
    crc.update(headerInfoSize);
    crc.update(headerInfo);

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
    
    stream.writeInt(headerInfoSize);
    stream.write(headerInfo);
    stream.writeLong(crc.getValue());
    
    file.append(byteArrayOutputStream.toByteArray());
  }

  private static void checkMemoriaTag(DataInputStream stream) throws IOException {
    if (stream.available() < FileLayout.MEMORIA_TAG.length) throw new MemoriaException("not a memoria file");
    byte[] buffer = new byte[FileLayout.MEMORIA_TAG.length];
    stream.read(buffer);
    if (!Arrays.equals(FileLayout.MEMORIA_TAG, buffer)) throw new MemoriaException("not a memoria file");
  }

  private static byte[] getCurrentBlockInfo(long blockPosition, int writeMode) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
    
    stream.writeInt(writeMode);
    stream.writeLong(blockPosition);
    MemoriaCRC32 crc = new MemoriaCRC32();
    crc.updateLong(blockPosition);
    
    stream.writeLong(crc.getValue());
    return byteArrayOutputStream.toByteArray();
  }

  private static Header readHeaderInfo(LastWrittenBlockInfo readCurrentBlockInfo, int headerInfoSize, byte[] headerInfo)
      throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(headerInfo));
    UUID thisUuid = readUuid(stream);
    UUID hostUuid = readUuid(stream);
    long hostBranchVersion = stream.readLong();
    Version version = readVersion(stream);
    int fileLayoutRevision = stream.readInt();

    String idFactoryClassName = stream.readUTF();
    String defaultInstantiatorClassName = stream.readUTF();
    
    return new Header(thisUuid, hostUuid, hostBranchVersion, version, fileLayoutRevision, idFactoryClassName,
        defaultInstantiatorClassName, FileLayout.getHeaderSize(headerInfoSize), readCurrentBlockInfo);
  }

  private static LastWrittenBlockInfo readLastWrittenBlockInfo(DataInputStream stream) throws IOException {
    int writeMode = stream.readInt();
    long currentBlock = stream.readLong();

    MemoriaCRC32 crc = new MemoriaCRC32();
    crc.updateLong(currentBlock);
    long readCrc = stream.readLong();
    if(readCrc != crc.getValue()) throw new MemoriaException("wrong crc in header");
    
    return new LastWrittenBlockInfo(writeMode, currentBlock);
  }

  private static UUID readUuid(DataInputStream stream) throws IOException {
    return new UUID(stream.readLong(), stream.readLong());
  }

  private static Version readVersion(DataInputStream stream) throws IOException {
    return new Version(stream.readInt(), stream.readInt(), stream.readInt());
  }

  /**
   * When no block was written, the last-written-block info is initialized with default-values.
   */
  private static void writeDefaultLastWrittenBlockInfo(IMemoriaFile file) throws IOException {
    byte[] byteArrayOutputStream = getCurrentBlockInfo(NO_CURRENT_BLOCK, FileLayout.WRITE_MODE_APPEND);
    
    file.append(byteArrayOutputStream);
  }
  
  private static byte[] writeHeaderInfo(CreateConfig config) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);

    writeUuid(stream, UUID.randomUUID());
    writeUuid(stream, Constants.NO_HOST_UUID);
    stream.writeLong(Constants.NO_HOST_BRANCH_REVISION);

    writeVersion(stream, Memoria.getMemoriaVersion());
    stream.writeInt(Memoria.getFileLayoutVersion());

    stream.writeUTF(config.getIdFactoryClassName());
    stream.writeUTF(config.getDefaultInstantiatorClassName());
    
    return byteArrayOutputStream.toByteArray();
  }

  private static void writeMemoriaTag(IMemoriaFile file) {
    if (file.getSize() != 0) throw new MemoriaException("file is not empty");
    file.append(FileLayout.MEMORIA_TAG);
  }

  private static void writeUuid(DataOutputStream stream, UUID uuid) throws IOException {
    ByteUtil.writeUUID(stream, uuid);
  }

  private static void writeVersion(DataOutputStream stream, Version version) throws IOException {
    stream.writeInt(version.getMajor());
    stream.writeInt(version.getMinor());
    stream.writeInt(version.getService());
  }

}
