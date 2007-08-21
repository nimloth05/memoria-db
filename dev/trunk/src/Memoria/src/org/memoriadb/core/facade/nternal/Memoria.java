package org.memoriadb.core.facade.nternal;

import java.util.*;

import org.memoriadb.core.*;
import org.memoriadb.core.backend.IMemoriaFile;
import org.memoriadb.core.facade.IMemoria;
import org.memoriadb.util.IdentityHashSet;

public class Memoria implements IMemoria {

  private final IObjectContainer fObjectContainer;
  
  private final Set<Object> fAdd = new IdentityHashSet<Object>();
  private final Set<Object> fUpdate = new IdentityHashSet<Object>();

  public Memoria(IObjectContainer objectContainer) {
    fObjectContainer = objectContainer;
  }

  @Override
  public void checkSanity() {
    fObjectContainer.checkSanity();
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
  public Collection<Object> getAllObjects() {
    return fObjectContainer.getAllObjects();
  }

  @Override
  public IMemoriaFile getFile() {
    return fObjectContainer.getFile();
  }

  @Override
  public IMetaClass getMetaClass(Object obj) {
    return fObjectContainer.getMetaClass(obj);
  }

  @Override
  public Object getObject(long id) {
    return fObjectContainer.getObject(id);
  } 

  @Override
  public long getObjectId(Object obj) {
    return fObjectContainer.getObjectId(obj);
  }

  @Override
  public long save(Object obj) {
    if(fAdd.contains(obj)){
      // added in same transaction
      return fObjectContainer.getObjectId(obj);
    }
    
    if(fObjectContainer.contains(obj)){
      // object already in the store, perform update. obj is replaced if several updates occur in same transaction.
      fUpdate.add(obj);
      return fObjectContainer.getObjectId(obj);
    }
    
    // object not already in the store, add it
    fAdd.add(obj);
    addMetaClassIfNecessary(obj);
    return fObjectContainer.add(obj);
  } 

  public long saveAll(Object root) {
    ObjectTraversal traversal = new ObjectTraversal(this);
    traversal.handle(root);
    return fObjectContainer.getObjectId(root);
  }

  @Override
  public void writePendingChanges() {
    IdentityHashSet<Object> save = new IdentityHashSet<Object>();
    save.addAll(fAdd);
    save.addAll(fUpdate);
    
    fObjectContainer.write(save); 
  }

  private void addMetaClassIfNecessary(Object obj) {
    Class<?> klass = obj.getClass();
    
    // if obj is an array, the metaClass of the componentType is added. 
    // The MetaClass for the array is generic and bootstrapped
    if(klass.isArray()){
      klass = klass.getComponentType();
    }
    
    if(fObjectContainer.metaClassExists(klass)) return;
    IMetaClass metaClass = fObjectContainer.createMetaClass(klass);
    save(metaClass);
  }

}
