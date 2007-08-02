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
  
  
  public MetaClass registerClassObject(DataOutput dataStream, Class<?> type) throws Exception  {
    MetaClass classObject = fObjectRepo.getMetaObject(type);
    
    if (classObject == null) {
      classObject = new MetaClass(type);
      serializeObject(dataStream, classObject);
    }
    return classObject;
  }

  @Override
  public long serializeIfNotContained(Object referencee) {
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
    for(Object object: fObjectsToSerialize) {
      serializeObject(stream, object);
    }
    return buffer.toByteArray();
  }

  private void serializeMetaClass(DataOutput objectStream, MetaClass classObject) throws IOException {
    objectStream.writeUTF(classObject.getClassName());
    serializeMetaFields(objectStream, classObject);
  }
  
  private void serializeMetaFields(DataOutput objectStream, MetaClass classObject) throws IOException {
    for(MetaField field: classObject.getFields()) {
      objectStream.writeInt(field.getId());
      objectStream.writeUTF(field.getName());
      objectStream.writeInt(field.getType());
    }
  }
  
  private void serializeObject(DataOutput dataStream, Object object) throws Exception {
    Class<?> type = object.getClass();
    long objectId = fObjectRepo.register(object);
    
    MetaClass metaClass = registerClassObject(dataStream, type);
    
    serializeObject(metaClass, dataStream, object, objectId);
  }

  private void serializeObject(MetaClass classObject, DataOutput dataStream, Object object, long objectId) throws Exception {
    long typeId = fObjectRepo.getObjectId(classObject);
    
    ByteArrayOutputStream buffer = new ByteArrayOutputStream(80);
    DataOutputStream objectStream = new DataOutputStream(buffer);
    
    objectStream.writeLong(typeId);
    objectStream.writeLong(objectId);
    
    if(MetaClass.isMetaClassObject(typeId)) {
      serializeMetaClass(objectStream, (MetaClass) object);
    }
    else {
      serializeObjectValues(objectStream, classObject, object);
    }
    
    byte[] objectData = buffer.toByteArray();
    dataStream.writeInt(objectData.length);
    dataStream.write(objectData);    
  }
  
  private void serializeObjectValues(DataOutputStream stream, MetaClass classObject, Object object) throws Exception  {
    for(MetaField metaField: classObject.getFields()) {
      stream.writeInt(metaField.getId());
      metaField.getFieldType().writeValue(stream, object, metaField.getJavaField(object), this);
    }
  }
  
}
