package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.Constants;

/**
 * Call serialize(Object) for each object, then call getBytes(). After this, the ObjectSerializer is gone.
 * 
 * @author msc
 * 
 */
//FIXME: Kann man hier diese Schnittstelle entfernen und sie "private" (inner class) implementieren?
public final class ObjectSerializer implements ISerializeContext {

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

  @Override
  public boolean contains(Object obj) {
    return fObjectRepo.contains(obj);
  }

  public byte[] getBytes() {
    return fBuffer.toByteArray();
  }

  
  @Override
  public IObjectId getMemoriaClassId(String className) {
    IMemoriaClassConfig memoriaClass = fObjectRepo.getMemoriaClass(className);
    return fObjectRepo.getObjectId(memoriaClass);
  }

  @Override
  public IObjectId getNullReference() {
    return fObjectRepo.getNullReference();
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
      internalMarkObjectAsDeleted(fObjectRepo.getObjectInfoForId(id));
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  //FIXME: Aussen hat man bereits das ObjectInfo, es könnte direkt übergeben werden.
  public void serialize(Object obj) {
    try {
      serializeObject(fObjectRepo.getObjectInfo(obj));
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  private void internalMarkObjectAsDeleted(IObjectInfo info) throws IOException {
    fStream.writeInt(2*fObjectRepo.getIdFactory().getIdSize());
    IObjectId typeId = fObjectRepo.isMemoriaClass(info.getObject()) ? fObjectRepo.getMemoriaClassDeletionMarker() : fObjectRepo.getObjectDeletionMarker();
    typeId.writeTo(fStream);
    info.getId().writeTo(fStream);
  }

  private void serializeObject(IObjectInfo info) throws Exception {
    //FIXME: Hier muss getExistingObject aufgerufen werden, da es die MemoriaClass geben muss!
    IMemoriaClass memoriaClass = (IMemoriaClass) fObjectRepo.getObject(info.getMemoriaClassId());
    serializeObject(memoriaClass.getHandler(), info);
  }

  private void serializeObject(ISerializeHandler handler, IObjectInfo info) throws Exception {
    IObjectId typeId = info.getMemoriaClassId();

    ByteArrayOutputStream buffer = new ByteArrayOutputStream(Constants.DEFAULT_OBJECT_SIZE);
    DataOutputStream objectStream = new DataOutputStream(buffer);

    typeId.writeTo(objectStream);
    info.getId().writeTo(objectStream);
    
    handler.serialize(info.getObject(), objectStream, this);

    byte[] objectData = buffer.toByteArray();
    fStream.writeInt(objectData.length);
    fStream.write(objectData);
  }

}
