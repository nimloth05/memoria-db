package org.memoriadb.core;

import java.io.*;
import java.util.*;

import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdentityHashSet;

public class ObjectSerializer implements ISerializeContext {
  
  
  private final ObjectRepo fObjectRepo;
  
  /**
   * Additional Objects that must be serialized to complete the aggregate.
   */
  private final Set<Object> fObjectsToSerialize = new IdentityHashSet<Object>();

  /**
   * The objects to write.
   */
  private final Set<Object> fObjects = new IdentityHashSet<Object>();
  
  public static byte[] serialize(ObjectRepo objectRepo, List<Object> objects) {
    return new ObjectSerializer(objectRepo, objects).serializeObjects();
  }
  
  public ObjectSerializer(ObjectRepo repo, List<Object> objects) {
    fObjectRepo = repo;
    fObjects.addAll(objects);
  }
  
  
  public IMetaClass registerClassObject(DataOutput dataStream, Class<?> type) throws Exception  {
    IMetaClass classObject = fObjectRepo.getMetaObject(type);
    
    if (classObject == null) {
      classObject = new MetaClass(type);
      serializeObject(dataStream, classObject);
    }
    return classObject;
  }

  @Override
  public long serializeIfNotContained(Object referencee) {
    if (referencee instanceof Class) {
      Class<?> clazz = (Class) referencee;
      referencee = fObjectRepo.getMetaObject(clazz);
      if (referencee == null) {
        referencee = new MetaClass(clazz);
      } 
    }
    
    if (fObjectRepo.contains(referencee)) return fObjectRepo.getObjectId(referencee);
    long result = fObjectRepo.register(referencee);
    if (fObjects.contains(referencee)) return result;
    fObjectsToSerialize.add(referencee);
    return result;
  }
  
  public byte[] serializeObjects() {
    try {
      return internalSerializeObjects();
    } catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  private byte[] internalSerializeObjects() throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    DataOutput stream = new DataOutputStream(buffer);
    
    for(Object object: fObjects) {
      serializeObject(stream, object);
    }

    while(!fObjectsToSerialize.isEmpty()) {
      Iterator<Object> iterator = fObjectsToSerialize.iterator();
      Object next = iterator.next();
      iterator.remove();
      serializeObject(stream, next);
    }
    
    return buffer.toByteArray();
  }

  private void serializeObject(DataOutput dataStream, Object object) throws Exception {
    long objectId = fObjectRepo.register(object);
    
    Class<?> type = object.getClass();
    IMetaClass metaClass = registerClassObject(dataStream, type);
    
    serializeObject(metaClass, dataStream, object, objectId);
  }

  private void serializeObject(IMetaClass classObject, DataOutput dataStream, Object object, long objectId) throws Exception {
    long typeId = fObjectRepo.getObjectId(classObject);
    
    ByteArrayOutputStream buffer = new ByteArrayOutputStream(80);
    DataOutputStream objectStream = new DataOutputStream(buffer);
    
    objectStream.writeLong(typeId);
    objectStream.writeLong(objectId);
    
    classObject.getHandler().serialize(object, objectStream, this);
    
    byte[] objectData = buffer.toByteArray();
    dataStream.writeInt(objectData.length);
    dataStream.write(objectData);    
  }
  
}
