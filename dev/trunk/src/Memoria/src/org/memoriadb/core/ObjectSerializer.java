package org.memoriadb.core;

import java.io.*;
import java.util.Set;

import org.memoriadb.core.meta.IMetaClass;
import org.memoriadb.core.repo.ObjectInfo;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdentityHashSet;

public class ObjectSerializer implements ISerializeContext {
  
  private final IObjectRepo fObjectRepo;
  
  /**
   * The objects to write. 
   */
  private final Set<Object> fObjects;
  
  public static byte[] serialize(IObjectRepo objectRepo, IdentityHashSet<Object> objects) {
    return new ObjectSerializer(objectRepo, objects).serializeObjects();
  }
  
  /**
   * @param repo
   */
  public ObjectSerializer(IObjectRepo repo, IdentityHashSet<Object> objects) {
    fObjectRepo = repo;
    fObjects = objects;
  }
  
  
  @Override
  public long getMetaClassId(Class<?> klass) {
    IMetaClass metaClass = fObjectRepo.getMetaClass(klass);
    return fObjectRepo.getObjectId(metaClass);
  }
  
  @Override
  public long getObjectId(Object obj) {
    return fObjectRepo.getObjectId(obj);
  }

  private byte[] internalSerializeObjects() throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    DataOutput stream = new DataOutputStream(buffer);
    
    for(Object object: fObjects) {
      serializeObject(stream, fObjectRepo.getObjectInfo(object));
    }

    return buffer.toByteArray();
  }

  private void serializeObject(DataOutput dataStream, ObjectInfo info) throws Exception {
    IMetaClass metaClass = fObjectRepo.getMetaClass(info.getObj().getClass());
    serializeObject(metaClass, dataStream, info);
  }

  private void serializeObject(IMetaClass classObject, DataOutput dataStream, ObjectInfo info) throws Exception {
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

  private byte[] serializeObjects() {
    try {
      return internalSerializeObjects();
    } catch (Exception e) {
      throw new MemoriaException(e);
    }
  }
  
}
