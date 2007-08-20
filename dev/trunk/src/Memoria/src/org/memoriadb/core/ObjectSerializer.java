package org.memoriadb.core;

import java.io.*;
import java.util.Set;

import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdentityHashSet;

public class ObjectSerializer implements ISerializeContext {
  
  private final ObjectRepo fObjectRepo;
  
  /**
   * The objects to write. 
   */
  private final Set<Object> fObjects;
  
  public static byte[] serialize(ObjectRepo objectRepo, IdentityHashSet<Object> objects) {
    return new ObjectSerializer(objectRepo, objects).serializeObjects();
  }
  
  /**
   * @param repo
   */
  public ObjectSerializer(ObjectRepo repo, IdentityHashSet<Object> objects) {
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

  public IMetaClass registerClassObject(DataOutput dataStream, Class<?> type) throws Exception  {
    IMetaClass classObject = fObjectRepo.getMetaClass(type);
    
    if (classObject == null) {
      classObject = new MetaClass(type);
      //serializeObject(dataStream, classObject);
    }
    return classObject;
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
      serializeObject(stream, fObjectRepo.getObjectInfo(object));
    }

    // wird in die Transaction verschoben,msc
    
//    while(!fObjectsToSerialize.isEmpty()) {
//      Iterator<Object> iterator = fObjectsToSerialize.iterator();
//      Object next = iterator.next();
//      iterator.remove();
//      serializeObject(stream, next);
//    }
    
    return buffer.toByteArray();
  }

  private void serializeObject(DataOutput dataStream, ObjectInfo info) throws Exception {
//    // Im moment wird hier das obj noch zum repo hinzugefügt, entfernen. msc
//    long objectId = fObjectRepo.register(info.getObj());
//    
//    Class<?> type = info.getObj().getClass();
//    // auch das wird anders gelöst, die Metaclass wird früher erzeugt...
//    IMetaClass metaClass = registerClassObject(dataStream, type);
    IMetaClass metaClass = fObjectRepo.getMetaClass(info.getObj().getClass());
    serializeObject(metaClass, dataStream, info);
  }

  private void serializeObject(IMetaClass classObject, DataOutput dataStream, ObjectInfo info) throws Exception {
    long typeId = fObjectRepo.getObjectId(classObject);
    
    ByteArrayOutputStream buffer = new ByteArrayOutputStream(80);
    DataOutputStream objectStream = new DataOutputStream(buffer);
    
    objectStream.writeLong(typeId);
    objectStream.writeLong(info.getId());
    
    classObject.getHandler().serialize(info.getObj(), objectStream, this);
    
    byte[] objectData = buffer.toByteArray();
    dataStream.writeInt(objectData.length);
    dataStream.write(objectData);    
  }
  
}
