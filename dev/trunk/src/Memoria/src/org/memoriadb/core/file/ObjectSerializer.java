package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.Constants;

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
  public IObjectId getMemoriaClassId(Class<?> klass) {
    IMemoriaClass memoriaClass = fObjectRepo.getMemoriaClass(klass);
    return fObjectRepo.getObjectId(memoriaClass);
  }

  @Override
  public IObjectId getObjectId(Object obj) {
    return fObjectRepo.getObjectId(obj);
  }

  @Override
  public IObjectId getRootClassId() {
    return fObjectRepo.getRootClassId();
  }
  
  public void markAsDeleted(IObjectId id) {
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
    IObjectId typeId = fObjectRepo.isMetaClass(info.getObj()) ? fObjectRepo.getMemoriaClassDeletionMarker() : fObjectRepo.getObjectDeletionMarker();
    typeId.writeTo(fStream);
    info.getId().writeTo(fStream);
    fStream.writeLong(info.getVersion());
  }

  private void serializeObject(DataOutput dataStream, IObjectInfo info) throws Exception {
    IMemoriaClass metaClass = fObjectRepo.getMemoriaClass(info.getObj().getClass());
    serializeObject(metaClass, dataStream, info);
  }

  private void serializeObject(IMemoriaClass memoriaClass, DataOutput dataStream, IObjectInfo info) throws Exception {
    IObjectId typeId = fObjectRepo.getObjectId(memoriaClass);

    ByteArrayOutputStream buffer = new ByteArrayOutputStream(Constants.DEFAULT_OBJECT_SIZE);
    DataOutputStream objectStream = new DataOutputStream(buffer);

    typeId.writeTo(objectStream);
    info.getId().writeTo(objectStream);
    objectStream.writeLong(info.getVersion());

    memoriaClass.getHandler().serialize(info.getObj(), objectStream, this);

    byte[] objectData = buffer.toByteArray();
    dataStream.writeInt(objectData.length);
    dataStream.write(objectData);
  }

}
