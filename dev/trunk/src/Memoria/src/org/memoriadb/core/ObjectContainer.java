package org.memoriadb.core;

import java.util.Collection;

import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.load.ObjectLoader;

public class ObjectContainer implements IContext, IObjectContainer {

  private final ObjectRepo fObjectRepo;
  private final IMemoriaFile fFile;

  public ObjectContainer(IMemoriaFile file) {
    fFile = file;
    fObjectRepo = ObjectRepoFactory.create();
    ObjectLoader.readIn(fFile, fObjectRepo);
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

  public ObjectRepo getObjecRepo() {
    return fObjectRepo;
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
  public long getSize() {
    return fFile.getSize();
  }

  @Override
  public boolean metaClassExists(Class<?> klass) {
    return fObjectRepo.metaClassExists(klass);
  }

  @Override
  public void update(Object obj) {
    fObjectRepo.update(obj);
  }

}
