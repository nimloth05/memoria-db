package org.memoriadb.core;

import java.util.*;

import org.memoriadb.IFilter;
import org.memoriadb.core.block.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.query.ClassModeQueryStrategy;
import org.memoriadb.exception.SchemaException;
import org.memoriadb.util.*;

public class ObjectStore implements IObjectStoreExt, IModeStrategy {

  private final TrxHandler fTrxHandler;
  private final ClassModeQueryStrategy fQueryStrategy = new ClassModeQueryStrategy();
  
  public ObjectStore(TrxHandler trxHandler) {
    fTrxHandler = trxHandler;
  }

  @Override
  public IObjectId addMemoriaClassIfNecessary(final TrxHandler trxHandler, Object obj) {
    
    if (obj.getClass().isArray()) {
      TypeInfo typeInfo = ReflectionUtil.getComponentTypeInfo(obj.getClass());
      if(typeInfo.getComponentType()==Type.typeClass) addTypeHierarchy(trxHandler, typeInfo.getJavaClass());
      return trxHandler.getIdFactory().getArrayMemoriaClass();
    }
    
    return addTypeHierarchy(trxHandler, obj.getClass());
    
  }

  @Override
  public void beginUpdate() {
    fTrxHandler.beginUpdate();
  }

  @Override
  public void checkCanInstantiateObject(ITrxHandler trxHandler, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator) {
    IMemoriaClass memoriaClass = (IMemoriaClass) trxHandler.getObject(memoriaClassId);
    
    Class<?> javaClass = ReflectionUtil.getClass(memoriaClass.getJavaClassName());
    if (ReflectionUtil.isNonStaticInnerClass(javaClass)) {
      throw new SchemaException("Can not save non-static inner classes " + javaClass);
    }
    
    memoriaClass.getHandler().checkCanInstantiateObject(memoriaClass.getJavaClassName(), defaultInstantiator);
  }

  @Override
  public void checkIndexConsistancy() {
    fTrxHandler.checkIndexConsistancy();
  }

  @Override
  public void close() {
    fTrxHandler.close();
  }

  @Override
  public boolean contains(Object obj) {
    return fTrxHandler.contains(obj);
  }

  @Override
  public boolean containsId(IObjectId id) {
    return fTrxHandler.contains(id);
  }

  @Override
  public void delete(Object obj) {
    fTrxHandler.delete(obj);
  }

  @Override
  public void deleteAll(Object root) {
    fTrxHandler.deleteAll(root);
  }

  @Override
  public void endUpdate() {
    fTrxHandler.endUpdate();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getAll(Class<T> clazz) {
    return fQueryStrategy.getAll(fTrxHandler.getObjectRepo(), clazz);
  }

  @Override
  public <T> List<T> getAll(Class<T> clazz, IFilter<T> filter) {
    return fQueryStrategy.getAll(fTrxHandler.getObjectRepo(), clazz, filter);
  }

  @Override
  public List<Object> getAll(String clazz) {
    return fQueryStrategy.getAll(fTrxHandler.getObjectRepo(), clazz);
  }

  @Override
  public List<Object> getAll(String clazz, IFilter<Object> filter) {
    return fQueryStrategy.getAll(fTrxHandler.getObjectRepo(), clazz, filter);
  }

  @Override
  public Collection<Object> getAllObjects() {
    return fTrxHandler.getAllObjects();
  }

  @Override
  public IBlockManager getBlockManager() {
    return fTrxHandler.getBlockManager();
  }

  public IMemoriaFile getFile() {
    return fTrxHandler.getFile();
  }

  @Override
  public FileHeader getHeader() {
    return fTrxHandler.getHeader();
  }

  @Override
  public long getHeadRevision() {
    return fTrxHandler.getHeadRevision();
  }

  @Override
  public IDefaultObjectIdProvider getIdFactory() {
    return fTrxHandler.getIdFactory();
  }

  @Override
  public int getIdSize() {
    return fTrxHandler.getIdSize();
  }

  @Override
  public IMemoriaClass getMemoriaClass(Class<?> clazz) {
    return fTrxHandler.getMemoriaClass(clazz.getName());
  }

  @Override
  public IMemoriaClass getMemoriaClass(Object obj) {
    return getObject(getMemoriaClassId(obj));
  }

  @Override
  public IObjectId getMemoriaClassId(Object obj) {
    return getObjectInfo(obj).getMemoriaClassId();
  }

  @Override
  public IObjectId getMemoriaFieldMetaClass() {
    return fTrxHandler.getMemoriaFieldMetaClass();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getObject(IObjectId id) {
    return (T) fTrxHandler.getObject(id);
  }

  @Override
  public IObjectId getObjectId(Object obj) {
    return fTrxHandler.getObjectId(obj);
  }

  @Override
  public ObjectInfo getObjectInfo(Object obj) {
    return fTrxHandler.getObjectInfo(obj);
  }

  @Override
  public IObjectInfo getObjectInfoForId(IObjectId id) {
    return fTrxHandler.getObjectInfoForId(id);
  }

  @Override
  public Set<ObjectInfo> getSurvivors(Block block) {
    return fTrxHandler.getSurvivors(block);
  }

  @Override
  public boolean isInDataMode() {
    return false;
  }

  @Override
  public boolean isInUpdateMode() {
    return fTrxHandler.isInUpdateMode();
  }

  public IObjectId save(Object obj) {
    return fTrxHandler.save(obj);
  }

  public IObjectId saveAll(Object root) {
    return fTrxHandler.saveAll(root);
  }
  
  
  public void writePendingChanges() {
    fTrxHandler.writePendingChanges();
  }
  
  void internalDelete(Object obj) {
    fTrxHandler.internalDelete(obj);
  }
  
  /* package */ IMemoriaClassConfig internalGetMemoriaClass(String klass) {
    return fTrxHandler.internalGetMemoriaClass(klass);
  }


  /**
   * Saves the obj without considering if this ObjectStore is in update-mode or not.
   */
  IObjectId internalSave(Object obj) {
    return fTrxHandler.internalSave(obj);
  }

  /**
   * @param trxHandler 
   * @return The id of the first (most derived) class in the hierarchy, because this is the
   * typeId of the object.
   * 
   * Idempotent, already stored classes are ignored.
   */
  private IObjectId addTypeHierarchy(TrxHandler trxHandler, Class<?> javaClass) {
    IMemoriaClassConfig classObject = trxHandler.internalGetMemoriaClass(javaClass.getName());

    // if the class is already in the store, all it's superclasses must also be known. Do nothing.
    if (classObject != null) {
      return trxHandler.getObjectId(classObject);
    }

    // add the current class and all its superclasses to the store
    classObject = MemoriaFieldClassFactory.createMetaClass(javaClass, trxHandler.getMemoriaFieldMetaClass());
    IObjectId result = trxHandler.internalSave(classObject);
    
    recursiveAddTypeHierarchy(trxHandler, javaClass, classObject);
    
    return result;
  }

  private void recursiveAddTypeHierarchy(TrxHandler trxHandler, Class<?> superClass, IMemoriaClassConfig subClassconfig) {
    Class<?> javaClass = superClass.getSuperclass();
    if(javaClass == null) return;

    // the super-class may already be there (bootstrapped, other hierarchy-branch)
    IMemoriaClassConfig classObject = trxHandler.internalGetMemoriaClass(javaClass.getName());
    if(classObject != null){
      subClassconfig.setSuperClass(classObject);
      return;
    }
    
    classObject = MemoriaFieldClassFactory.createMetaClass(javaClass, trxHandler.getMemoriaFieldMetaClass());
    trxHandler.internalSave(classObject);
    subClassconfig.setSuperClass(classObject);
    
    recursiveAddTypeHierarchy(trxHandler, javaClass, classObject);
  }
}
