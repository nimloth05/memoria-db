package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

/**
 * Call serialize(Object) for each object, then call getBytes(). After this, the ObjectSerializer is gone.
 * 
 * @author msc
 * 
 */
public class ObjectSerializer implements ISerializeContext {

  private final IObjectRepo fObjectRepo;

  private final ByteArrayOutputStream fBuffer;

  private final DataOutput fStream;

  /**
   * @param repo
   */
  public ObjectSerializer(IObjectRepo repo) {
    fObjectRepo = repo;
    fBuffer = new ByteArrayOutputStream();
    fStream = new DataOutputStream(fBuffer);
  }

  public byte[] getBytes() {
    return fBuffer.toByteArray();
  }

  @Override
  public long getMetaClassId(Class<?> klass) {
    IMemoriaClass memoriaClass = fObjectRepo.getMemoriaClass(klass);
    return fObjectRepo.getObjectId(memoriaClass);
  }

  @Override
  public long getObjectId(Object obj) {
    return fObjectRepo.getObjectId(obj);
  }

  public void markAsDeleted(long id) {
    try {
      internalMarkObjectAsDeleted(fObjectRepo.getObjectInfo(id));
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }
  
  public void serialize(Object obj) {
    try {
      serializeObject(fStream, fObjectRepo.getObjectInfo(obj));
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  private void internalMarkObjectAsDeleted(IObjectInfo info) throws IOException {
    fStream.writeInt(3*Constants.LONG_SIZE);
    fStream.writeLong(fObjectRepo.isMetaClass(info.getObj())? IdConstants.METACLASS_DELETED : IdConstants.OBJECT_DELETED);
    fStream.writeLong(info.getId());
    fStream.writeLong(info.getVersion());
  }

  private void serializeObject(DataOutput dataStream, IObjectInfo info) throws Exception {
    IMemoriaClass metaClass = fObjectRepo.getMemoriaClass(info.getObj().getClass());
    serializeObject(metaClass, dataStream, info);
  }

  private void serializeObject(IMemoriaClass classObject, DataOutput dataStream, IObjectInfo info) throws Exception {
    long typeId = fObjectRepo.getObjectId(classObject);

    ByteArrayOutputStream buffer = new ByteArrayOutputStream(80);
    DataOutputStream objectStream = new DataOutputStream(buffer);

    objectStream.writeLong(typeId);
    objectStream.writeLong(info.getId());
    objectStream.writeLong(info.getVersion());

    classObject.getHandler().serialize(info.getObj(), objectStream, this);

    byte[] objectData = buffer.toByteArray();
    dataStream.writeInt(objectData.length);
    dataStream.write(objectData);
  }

}
