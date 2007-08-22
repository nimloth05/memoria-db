package org.memoriadb.core;

import java.io.*;
import java.util.Collection;

import org.memoriadb.core.backend.IMemoriaFile;
import org.memoriadb.core.load.FileReader;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdentityHashSet;

public class ObjectContainer implements IContext, IObjectContainer { 
  
  private final ObjectRepo fObjectRepo;
  
  private final IMemoriaFile fFile;
  
  public ObjectContainer(IMemoriaFile file) {
    fFile = file;
    fObjectRepo = ObjectRepoFactory.create();
    FileReader.readIn(fFile, fObjectRepo);
  }
  
  @Override
  public long add(Object obj) {
    return fObjectRepo.add(obj);
  } 

  public void checkSanity() {
    fObjectRepo.checkSanity();
  }

  @Override
  public void close() {
    fFile.close();
  }

  @Override
  public boolean contains(long id) {
    return fObjectRepo.contains(id);
  }

  @Override
  public boolean contains(Object obj) {
    return fObjectRepo.contains(obj);
  }

  @Override
  public IMetaClass createMetaClass(Class<?> klass) {
    return fObjectRepo.createMetaClass(klass);
  }

  public Collection<Object> getAllObjects() {
    return fObjectRepo.getAllObjects();
  }

  @Override
  public IMemoriaFile getFile() {
    return fFile;
  }

  @Override
  public IMetaClass getMetaClass(Object obj) {
    return fObjectRepo.getMetaClass(obj.getClass());
  }

  @Override
  public Object getObject(long id) {
    return fObjectRepo.getObject(id);
  }

  @Override
  public long getObjectId(Object obj) {
    return fObjectRepo.getObjectId(obj);
  }

  @Override
  public int getSize() {
    return fFile.getSize();
  }

  @Override
  public boolean metaClassExists(Class<?> klass) {
    return fObjectRepo.metaClassExists(klass);
  }

  public void write(IdentityHashSet<Object> objects) {
    try {
      append(ObjectSerializer.serialize(fObjectRepo, objects));
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  private void append(byte[] data) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream file = new DataOutputStream(byteArrayOutputStream);
    
    file.write(HeaderUtil.BLOCK_START_TAG);
    int size = data.length+HeaderUtil.TRANSACTION_START_TAG.length+4+HeaderUtil.TRANSACTION_END_TAG.length; //the block is as big as the transaction data.
    file.writeInt(size); 
    file.write(HeaderUtil.TRANSACTION_START_TAG);
    file.writeInt(data.length);
    
    file.write(data);
    
    file.write(HeaderUtil.TRANSACTION_END_TAG);
    file.write(HeaderUtil.BLOCK_END_TAG);
    
    fFile.append(byteArrayOutputStream.toByteArray());
    
  }

}
