package org.memoriadb.core;

import java.io.*;
import java.util.*;

public class ObjectContainer implements IContext, IObjectContainer {
  
  private final ObjectRepo fObjectRepo;
  
  private final File fFile;
  
  /**
   * Additional Objects that must be serialized to complete the aggregate.
   */
  private final List<Object> fObjectsToSerialize = new ArrayList<Object>();
  
  ObjectContainer(File file) {
    fFile = file;
    fObjectRepo = ObjectRepoFactory.create();
  }
  
  public void checkSanity() {
    fObjectRepo.checkSanity();
  }

  @Override
  public boolean contains(Object obj) {
    return fObjectRepo.contains(obj);
  }

  public Collection<Object> getAllObjects() {
    return fObjectRepo.getAllObjects();
  }

  public Collection<MetaClass> getMetaClass() {
    return fObjectRepo.getMetaObejcts();
  }

  @Override
  public MetaClass getMetaObject(Class<?> javaType) {
    return fObjectRepo.getMetaObject(javaType);
  }

  @Override
  public Object getObjectById(long objectId) {
    return fObjectRepo.getObjectById(objectId);
  }

  @Override
  public long getObjectId(Object obj) {
    return fObjectRepo.getObjectId(obj);
  }

  public void open() {
    FileReader.readIn(fFile, fObjectRepo);
  }

  @Override
  public void put(long objectId, Object obj) {
    fObjectRepo.put(objectId, obj);
  }

  @Override
  public long register(Object object) {
    return fObjectRepo.register(object);
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
  public void serializeIfNotContained(Object referencee) throws Exception {
    //FIXME This Implementation is bad.
    if (fObjectRepo.contains(referencee)) return;
    fObjectsToSerialize.add(referencee);
  }
  
  public void serializeObject(DataOutput dataStream, Object object) throws Exception {
    Class<?> type = object.getClass();
    long objectId = fObjectRepo.register(object);
    
    MetaClass metaClass = registerClassObject(dataStream, type);
    
    metaClass.writeObject(this, dataStream, object, objectId);
  }

  public void writeObject(List<Object> objects) {
    try {
      internalWriteObject(objects);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("exception occured during write back: " + e);
    }
  }

  private void append(byte[] data) throws IOException {
    RandomAccessFile file = new RandomAccessFile(fFile, "rw");
    
    long length = file.length();
    if (length > 0) {
      file.seek(file.length());
    }
    
    file.write(HeaderUtil.BLOCK_START_TAG);
    int size = data.length+HeaderUtil.TRANSACTION_START_TAG.length+4+HeaderUtil.TRANSACTION_END_TAG.length; //the block is as big as the transaction data.
    file.writeInt(size); 
    file.write(HeaderUtil.TRANSACTION_START_TAG);
    file.writeInt(data.length);
    
    file.write(data);
    
    file.write(HeaderUtil.TRANSACTION_END_TAG);
    file.write(HeaderUtil.BLOCK_END_TAG);
    
    file.getFD().sync();
    file.close();
  }

  private void internalWriteObject(List<Object> objects) throws Exception {
    byte[] data = serializeObjects(objects);
    byte[] additionalObjects = serializeObjects(fObjectsToSerialize);
    
    byte[] result = new byte[data.length + additionalObjects.length];
    
    System.arraycopy(data, 0, result, 0, data.length);
    System.arraycopy(additionalObjects, 0, result, data.length, additionalObjects.length);
    
    append(result);
  }


  private byte[] serializeObjects(List<Object> objects) throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    DataOutput stream = new DataOutputStream(buffer);
    for(Object object: objects) {
      serializeObject(stream, object);
    }
    return buffer.toByteArray();
  }
  
}
