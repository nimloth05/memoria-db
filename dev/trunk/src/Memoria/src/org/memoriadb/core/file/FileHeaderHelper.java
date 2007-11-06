package org.memoriadb.core.file;

import java.io.*;
import java.util.*;

import org.memoriadb.Memoria;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

public class FileHeaderHelper {
  
  public static final byte[] MEMORIA_TAG = new byte[]{'m', 'e', 'm', 'o', 'r', 'i', 'a'};
  
  public static FileHeader readHeader(InputStream inputStream) throws IOException {
    DataInputStream stream = new DataInputStream(inputStream);
    
    if(stream.available() < MEMORIA_TAG.length) throw new MemoriaException("not a memoria file");
    byte[] buffer = new byte[MEMORIA_TAG.length];
    stream.read(buffer);
    if(!Arrays.equals(MEMORIA_TAG, buffer)) throw new MemoriaException("not a memoria file");
    
    UUID thisUuid = readUuid(stream);
    UUID hostUuid = readUuid(stream);
    
    Version version = readVersion(stream);
    
    int fileLayoutVersion = stream.readInt();
    
    String idFactoryClassName = stream.readUTF();
    
    int headerSize = stream.readInt();
    
    return new FileHeader(thisUuid, hostUuid, version, fileLayoutVersion, idFactoryClassName, headerSize);
    
  }
  
  public static void writeHeader(IMemoriaFile file, String idFactoryClassName) throws IOException {
    if(file.getSize() != 0) throw new MemoriaException("file is not empty");
    
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
    
    stream.write(MEMORIA_TAG);
    
    writeUuid(stream, UUID.randomUUID());
    writeUuid(stream, Constants.NO_HOST_UUID);
    
    writeVersion(stream, Memoria.getMemoriaVersion());
    stream.writeInt(Memoria.getFileLayoutVersion());
    
    stream.writeUTF(idFactoryClassName);
    
    int headerSize = byteArrayOutputStream.size() + 4; // plus size of this int
    stream.writeInt(headerSize); 
    
    byte[] data = byteArrayOutputStream.toByteArray();
    if(data.length != headerSize) throw new MemoriaException("wrong headerSize");
    file.append(data);
  }
  
  private static UUID readUuid(DataInputStream stream) throws IOException {
    return new UUID(stream.readLong(), stream.readLong());
  }
  
  private static Version readVersion(DataInputStream stream) throws IOException {
    return new Version(stream.readInt(), stream.readInt(), stream.readInt());
  }
  
  private static void writeUuid(DataOutputStream stream, UUID uuid) throws IOException{
    stream.writeLong(uuid.getMostSignificantBits());
    stream.writeLong(uuid.getLeastSignificantBits());
  }
  
  private static void writeVersion(DataOutputStream stream, Version version) throws IOException {
    stream.writeInt(version.getMajor());
    stream.writeInt(version.getMinor());
    stream.writeInt(version.getService());
  }
  
}
