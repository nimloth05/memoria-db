package org.memoriadb.core;

import java.util.*;

import org.memoriadb.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdentityHashSet;

public class ObjectStore implements IObjectStore {

  private final IObjectRepo fObjectRepo;
  private final IFileWriter fFileWriter;

  private final Set<Object> fAdd = new IdentityHashSet<Object>();
  private final Set<Object> fUpdate = new IdentityHashSet<Object>();
  private final Set<Long> fDelete = new IdentityHashSet<Long>();

  private final IMemoriaFile fFile;

  private int fUpdateCounter = 0;

  public ObjectStore(IObjectRepo objectContainer, IMemoriaFile file) {
    fObjectRepo = objectContainer;
    fFile = file;
    FileWriter fileWriter = new FileWriter(file);
    fFileWriter = fileWriter;
  }

  @Override
  public void beginUpdate() {
    ++fUpdateCounter;
  }

  @Override
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
  public void delete(Object obj) {
    internalDelete(obj);
    if (!isInUpdateMode()) writePendingChanges();
  }

  @Override
  public void deleteAll(Object root) {
    internalDeleteAll(root);
    if (!isInUpdateMode()) writePendingChanges();
  }

  @Override
  public void endUpdate() {
    if (fUpdateCounter == 0) throw new MemoriaException("ObjectStore is not in update-mode");
    --fUpdateCounter;
    if (fUpdateCounter != 0) return;

    writePendingChanges();
  }

  @Override
  public <T> List<T> getAll(Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    for (Object object : getAllObjects()) {
      if (clazz.isInstance(object)) result.add(clazz.cast(object));
    }
    return result;
  }

  @Override
  public <T> List<T> getAll(Class<T> clazz, IFilter<T> filter) {
    List<T> result = new ArrayList<T>();
    for (Object object : getAllObjects()) {
      if (clazz.isInstance(object)) {
        T t = clazz.cast(object);
        if (filter.accept(t)) result.add(t);
      }
    }
    return result;
  }

  @Override
  public Collection<Object> getAllObjects() {
    return fObjectRepo.getAllObjects();
  }

  public IMemoriaFile getFile() {
    return fFile;
  }

  @Override
  public IMetaClass getMetaClass(Class<?> clazz) {
    return fObjectRepo.getMetaClass(clazz);
  }
  
  @Override
  public IMetaClass getMetaClass(Object obj) {
    return getMetaClass(obj.getClass());
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getObject(long id) {
    return (T) fObjectRepo.getObject(id);
  }

  @Override
  public long getObjectId(Object obj) {
    return fObjectRepo.getObjectId(obj);
  }

  @Override
  public IObjectInfo getObjectInfo(long id) {
    return fObjectRepo.getObjectInfo(id); 
  }

  @Override
  public IObjectInfo getObjectInfo(Object obj) {
    return fObjectRepo.getObjectInfo(obj);
  }

  public long getSize() {
    return fFile.getSize();
  }

  @Override
  public boolean isInUpdateMode() {
    return fUpdateCounter > 0;
  }

  public long save(Object obj) {
    long result = internalSave(obj);
    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  @Override
  public long[] save(Object... objs) {
    long[] result = new long[objs.length];

    for (int i = 0; i < objs.length; ++i) {
      result[i] = internalSave(objs[i]);
    }

    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  public long saveAll(Object root) {
    long result = internalSaveAll(root);
    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  public long[] saveAll(Object... roots) {
    long[] result = new long[roots.length];

    for (int i = 0; i < roots.length; ++i) {
      result[i] = internalSaveAll(roots[i]);
    }

    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  public void writePendingChanges() {
    if(fAdd.isEmpty() && fUpdate.isEmpty() && fDelete.isEmpty()) return;
    
    ObjectSerializer serializer = new ObjectSerializer(fObjectRepo);
    for(Object obj: fAdd) {
      serializer.serialize(obj);
    }
    for(Object obj: fUpdate) {
      fObjectRepo.objectUpdated(obj);
      serializer.serialize(obj);
    }
    for(Long id: fDelete){
      fObjectRepo.objectDeleted(id);
      serializer.markAsDeleted(id);
    }
    
    fFileWriter.write(serializer.getBytes());
    
    fAdd.clear();
    fUpdate.clear();
    fDelete.clear();
  }

  void internalDelete(Object obj) {
    if(!fObjectRepo.contains(obj)) return;
    
    if(fAdd.remove(obj)){
      // object was added in current transaction, remove it from add-list and from the repo
      fObjectRepo.delete(obj);
      return;
    }
    
    // if object was previously updated in current transaction, remove it from update-list
    fUpdate.remove(obj);
    
    long id = fObjectRepo.delete(obj);
    fDelete.add(id);
  }

  /**
   * Saves the obj without considering if this ObjectStore is in update-mode or not.
   */
  long internalSave(Object obj) {
    
    // if the object was previous removed in the current transaction, the remove-marker will not be set
    // and the object is updated.
    if(fDelete.remove(obj)) {
      fUpdate.add(obj);
      return fObjectRepo.getObjectId(obj); 
    }
    
    if (fAdd.contains(obj)) {
      // added in same transaction
      return fObjectRepo.getObjectId(obj);
    }

    if (fObjectRepo.contains(obj)) {
      // object already in the store, perform update. obj is replaced if several updates occur in same transaction.
      fUpdate.add(obj);
      return fObjectRepo.getObjectId(obj);
    }

    // object not already in the store, add it
    fAdd.add(obj);
    addMetaClassIfNecessary(obj);
    return fObjectRepo.add(obj);
  }
  
  private void addMetaClassIfNecessary(Object obj) {
    Class<?> klass = obj.getClass();

    // if obj is an array, the metaClass of the componentType is added.
    // The MetaClass for the array is generic and bootstrapped
    if (klass.isArray()) {
      klass = klass.getComponentType();
    }

    new InheritanceTraverser(klass) {

      private IMetaClassConfig fChildClass = null;

      @Override
      public void handle(Class<?> clazz) {
        IMetaClassConfig classObject = fObjectRepo.getMetaClass(clazz);

        if (classObject != null) {
          if (fChildClass != null) fChildClass.setSuperClass(classObject);
          abort();
          return;
        }

        classObject = MetaObjectFactory.createMetaClass(clazz);
        if (fChildClass != null) fChildClass.setSuperClass(classObject);
        fChildClass = classObject;
        internalSave(classObject);
      }
    };
  }

  private void internalDeleteAll(Object root) {
    DeleteTraversal traversal = new DeleteTraversal(this);
    traversal.handle(root);
  }

  private long internalSaveAll(Object root) {
    SaveTraversal traversal = new SaveTraversal(this);
    traversal.handle(root);
    return fObjectRepo.getObjectId(root);
  }

}
