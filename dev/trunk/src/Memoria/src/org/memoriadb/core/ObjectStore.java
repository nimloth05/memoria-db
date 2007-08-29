package org.memoriadb.core;

import java.util.*;

import org.memoriadb.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdentityHashSet;

public class ObjectStore implements IObjectStore {

  private final IObjectRepo fObjectContainer;
  private final IFileWriter fFileWriter;
  
  private final Set<Object> fAdd = new IdentityHashSet<Object>();
  private final Set<Object> fUpdate = new IdentityHashSet<Object>();
  private final Set<Object> fDelete = new IdentityHashSet<Object>();
  
  private final IMemoriaFile fFile;
  
  private int fUpdateCounter = 0;

  public ObjectStore(IObjectRepo objectContainer, IMemoriaFile file) {
    fObjectContainer = objectContainer;
    fFile = file; 
    FileWriter fileWriter = new FileWriter(objectContainer, file);
    fFileWriter = fileWriter;
  }

  @Override
  public void beginUpdate() {
    ++fUpdateCounter;
  }

  @Override
  public void checkSanity() {
    fObjectContainer.checkSanity();
  }

  @Override
  public void close() {
    fFile.close();
  }

  @Override
  public boolean contains(long id) {
    return fObjectContainer.contains(id);
  }

  @Override
  public boolean contains(Object obj) {
    return fObjectContainer.contains(obj);
  }

  @Override
  public void endUpdate() {
    if(fUpdateCounter==0) throw new MemoriaException("ObjectStore is not in update-mode");
    --fUpdateCounter;
    if(fUpdateCounter != 0) return;
    
    writePendingChanges();
  }

  @Override
  public <T> List<T> getAll(Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    for(Object object: getAllObjects()) {
      if (clazz.isInstance(object)) result.add(clazz.cast(object));
    }
    return result;
  } 

  @Override
  public <T> List<T> getAll(Class<T> clazz, IFilter<T> filter) {
    List<T> result = new ArrayList<T>();
    for(Object object: getAllObjects()) {
      if (clazz.isInstance(object)) {
        T t = clazz.cast(object);
        if (filter.accept(t)) result.add(t);
      }
    }
    return result;
  }

  @Override
  public Collection<Object> getAllObjects() {
    return fObjectContainer.getAllObjects();
  } 

  public IMemoriaFile getFile() {
    return fFile;
  }

  @Override
  public IMetaClass getMetaClass(Object obj) {
    return fObjectContainer.getMetaClass(obj.getClass());
  }

  @Override
  public Object getObject(long id) {
    return fObjectContainer.getObject(id);
  }

  @Override
  public long getObjectId(Object obj) {
    return fObjectContainer.getObjectId(obj);
  }

  public long getSize() {
    return fFile.getSize();
  }
  
  @Override
  public boolean isInUpdateMode() {
    return fUpdateCounter > 0;
  }

  @Override
  public long save(Object... objs) {
    long result = -1;
    for(Object obj: objs){
      result = internalSave(obj);
    }
    if(!isInUpdateMode()) writePendingChanges();
    return result;
  }

  public long saveAll(Object root) {
    ObjectTraversal traversal = new ObjectTraversal(this);
    traversal.handle(root);
    if(!isInUpdateMode()) writePendingChanges();
    return fObjectContainer.getObjectId(root);
  }

  public void writePendingChanges() {
    IdentityHashSet<Object> save = new IdentityHashSet<Object>();
    save.addAll(fAdd);
    save.addAll(fUpdate);
    
    fFileWriter.write(save); 
    
    fAdd.clear();
    fUpdate.clear();
    fDelete.clear();
  }

  /**
   * Saves the obj without considering if this ObjectStore is in update-mode or not.
   */
  long internalSave(Object obj) {
    if(fAdd.contains(obj)){
      // added in same transaction
      return fObjectContainer.getObjectId(obj);
    }
    
    if(fObjectContainer.contains(obj)){
      // object already in the store, perform update. obj is replaced if several updates occur in same transaction.
      fUpdate.add(obj);
      fObjectContainer.update(obj);
      return fObjectContainer.getObjectId(obj);
    }
    
    // object not already in the store, add it
    fAdd.add(obj);
    addMetaClassIfNecessary(obj);
    return fObjectContainer.add(obj);
  }

  private void addMetaClassIfNecessary(Object obj) {
    Class<?> klass = obj.getClass();
    
    // if obj is an array, the metaClass of the componentType is added. 
    // The MetaClass for the array is generic and bootstrapped
    if(klass.isArray()){
      klass = klass.getComponentType();
    }
    
    new InheritanceTraverser(klass) {
      
      private IMetaClassConfig fChildClass = null;
      
      @Override
      public void handle(Class<?> clazz) {
        IMetaClassConfig classObject = fObjectContainer.getMetaClass(clazz);
        
        if(classObject != null) {
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

}
