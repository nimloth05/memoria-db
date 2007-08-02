package org.memoriadb.core;

import java.io.*;
import java.util.*;

import org.memoriadb.exception.MemoriaException;

public class ObjectContainer implements IContext, IObjectContainer {
  
  private final ObjectRepo fObjectRepo;
  
  private final File fFile;
  
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

  public void writeObject(List<Object> objects) {
    try {
      append(ObjectSerializer.serialize(fObjectRepo, objects));
    }
    catch (IOException e) {
      throw new MemoriaException(e);
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

}
