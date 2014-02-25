/*
 * Copyright 2010 memoria db project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package org.memoriadb.core.file;

import java.io.*;
import java.util.*;

import org.memoriadb.*;
import org.memoriadb.block.Block;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.util.*;
import org.memoriadb.core.util.io.*;

/**
 * @author Sandro
 */
public final class HeaderHelper {

  public static final long NO_CURRENT_BLOCK = -1;
  
  public static Header getHeader(IDataInput stream) throws IOException {
    checkMemoriaTag(stream);

    LastWrittenBlockInfo readCurrentBlockInfo = readLastWrittenBlockInfo(stream);

    MemoriaCRC32 crc = new MemoriaCRC32();

    int headerSize = stream.readInt();
    crc.updateInt(headerSize);

    byte[] headerInfo = new byte[headerSize];
    stream.readFully(headerInfo);
    
    long readCrc = stream.readLong();
    
    crc.update(headerInfo);
    if(readCrc != crc.getValue()) throw new MemoriaException("header corrupt, CRC32 check failed");
    
    return readHeaderInfo(readCurrentBlockInfo, headerSize, headerInfo);
  }

  /**
   * Stores the position of the last updated block in the file-header.
   *  
   * @param writeMode {@link FileLayout#WRITE_MODE_APPEND} or {@link FileLayout#WRITE_MODE_UPDATE}
   */
  public static void updateBlockInfo(IMemoriaFile file, Block block, int writeMode) throws IOException {
    byte[] data = getCurrentBlockInfo(block.getPosition(), writeMode);
    
    file.write(data, FileLayout.CURRENT_BLOCK_INFO_START_POSITION);
  }

  public static void writeHeader(IMemoriaFile file, CreateConfig config) throws IOException {
    writeMemoriaTag(file);
    
    writeDefaultLastWrittenBlockInfo(file);

    MemoriaDataOutputStream stream = new MemoriaDataOutputStream();

    stream.setMarker();
    writeHeaderInfo(config, stream);
    int numberOfBytesWritten = stream.size() - Constants.INT_LEN; 
    
    MemoriaCRC32 crc = new MemoriaCRC32();
    crc.updateInt(numberOfBytesWritten);
    stream.updateCRC32FromMarkerToPosition(crc);
    
    stream.writeAtMarker(numberOfBytesWritten);
    stream.writeLong(crc.getValue());
    
    file.append(stream.toByteArray());
  }

  private static void checkMemoriaTag(IDataInput stream) throws IOException {
    if (stream.available() < FileLayout.MEMORIA_TAG.length) throw new FileCorruptException("file too short for beeing a memoria file");
    byte[] buffer = new byte[FileLayout.MEMORIA_TAG.length];
    stream.readFully(buffer);
    if (!Arrays.equals(FileLayout.MEMORIA_TAG, buffer)) throw new FileCorruptException("not a memoria file");
  }

  private static byte[] getCurrentBlockInfo(long blockPosition, int writeMode) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
    
    MemoriaCRC32 crc = new MemoriaCRC32();
    
    stream.writeInt(writeMode);
    crc.updateInt(writeMode);

    stream.writeLong(blockPosition);
    crc.updateLong(blockPosition);
    
    stream.writeLong(crc.getValue());
    return byteArrayOutputStream.toByteArray();
  }

  private static Header readHeaderInfo(LastWrittenBlockInfo readCurrentBlockInfo, int headerInfoSize, byte[] headerInfo)
      throws IOException {
    IDataInput stream = new ByteBufferDataInput(headerInfo);
    UUID thisUuid = readUuid(stream);
    UUID hostUuid = readUuid(stream);
    long hostBranchVersion = stream.readLong();
    Version version = readVersion(stream);
    int fileLayoutRevision = stream.readInt();
    boolean useCompression = stream.readBoolean();
    
    String idFactoryClassName = stream.readUTF();
    String defaultInstantiatorClassName = stream.readUTF();
    
    ICompressor compressor = useCompression ? new ZipCompressor() : ICompressor.NullComporeesorInstance;
    
    return new Header(thisUuid, hostUuid, hostBranchVersion, version, fileLayoutRevision, idFactoryClassName,
        defaultInstantiatorClassName, FileLayout.getHeaderSize(headerInfoSize), readCurrentBlockInfo, compressor);
  }

  private static LastWrittenBlockInfo readLastWrittenBlockInfo(IDataInput stream) throws IOException {
    int writeMode = stream.readInt();
    long currentBlock = stream.readLong();

    MemoriaCRC32 crc = new MemoriaCRC32();
    crc.updateInt(writeMode);
    crc.updateLong(currentBlock);
    long readCrc = stream.readLong();
    if(readCrc != crc.getValue()) throw new FileCorruptException("wrong crc in header");
    
    return new LastWrittenBlockInfo(writeMode, currentBlock);
  }

  private static UUID readUuid(IDataInput stream) throws IOException {
    return new UUID(stream.readLong(), stream.readLong());
  }

  private static Version readVersion(IDataInput stream) throws IOException {
    return new Version(stream.readInt(), stream.readInt(), stream.readInt());
  }

  /**
   * When no block was written, the last-written-block info is initialized with default-values.
   */
  private static void writeDefaultLastWrittenBlockInfo(IMemoriaFile file) throws IOException {
    byte[] byteArrayOutputStream = getCurrentBlockInfo(NO_CURRENT_BLOCK, FileLayout.WRITE_MODE_APPEND);
    
    file.append(byteArrayOutputStream);
  }
  
  private static void writeHeaderInfo(CreateConfig config, DataOutput stream) throws IOException {
    writeUuid(stream, UUID.randomUUID());
    writeUuid(stream, Constants.NO_HOST_UUID);
    stream.writeLong(Constants.NO_HOST_BRANCH_REVISION);

    writeVersion(stream, Memoria.getMemoriaVersion());
    stream.writeInt(Memoria.getFileLayoutVersion());
    stream.writeBoolean(config.isUseCompression()); 

    stream.writeUTF(config.getIdFactoryClassName());
    stream.writeUTF(config.getDefaultInstantiatorClassName());
  }

  private static void writeMemoriaTag(IMemoriaFile file) {
    if (file.getSize() != 0) throw new MemoriaException("file is not empty");
    file.append(FileLayout.MEMORIA_TAG);
  }

  private static void writeUuid(DataOutput stream, UUID uuid) throws IOException {
    ByteUtil.writeUUID(stream, uuid);
  }

  private static void writeVersion(DataOutput stream, Version version) throws IOException {
    stream.writeInt(version.getMajor());
    stream.writeInt(version.getMinor());
    stream.writeInt(version.getService());
  }

  private HeaderHelper() {}

}
