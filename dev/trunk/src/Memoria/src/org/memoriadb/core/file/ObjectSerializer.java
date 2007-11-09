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
  private final DBMode fDbMode;

  /**
   * @param repo
   */
  public ObjectSerializer(IObjectRepo repo, DBMode dbMode) {
    fObjectRepo = repo;
    fDbMode = dbMode;
    fBuffer = new ByteArrayOutputStream();
    fStream = new DataOutputStream(fBuffer);
  }

  public byte[] getBytes() {
    return fBuffer.toByteArray();
  }

  @Override
  public DBMode getDBMode() {
    return fDbMode;
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
    //FIXME: Hier müssen wir zuerst in einen Buffer schreiben, da die ID-Grösse nicht mehr gegeben ist.
    fStream.writeInt(2*Constants.LONG_SIZE);
    IObjectId typeId = fObjectRepo.isMetaClass(info.getObj()) ? fObjectRepo.getMemoriaClassDeletionMarker() : fObjectRepo.getObjectDeletionMarker();
    typeId.writeTo(fStream);
    info.getId().writeTo(fStream);
  }

  private void serializeObject(DataOutput dataStream, IObjectInfo info) throws Exception {
    IMemoriaClass metaClass = (IMemoriaClass) fObjectRepo.getObject(info.getMemoriaClassId());
    serializeObject(metaClass, dataStream, info);
  }

  private void serializeObject(IMemoriaClass memoriaClass, DataOutput dataStream, IObjectInfo info) throws Exception {
    IObjectId typeId = fObjectRepo.getObjectId(memoriaClass);

    ByteArrayOutputStream buffer = new ByteArrayOutputStream(Constants.DEFAULT_OBJECT_SIZE);
    DataOutputStream objectStream = new DataOutputStream(buffer);

    typeId.writeTo(objectStream);
    info.getId().writeTo(objectStream);
    
    memoriaClass.getHandler().serialize(info.getObj(), objectStream, this);

    byte[] objectData = buffer.toByteArray();
    dataStream.writeInt(objectData.length);
    dataStream.write(objectData);
  }

}
