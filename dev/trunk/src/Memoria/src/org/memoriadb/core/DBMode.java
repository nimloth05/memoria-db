package org.memoriadb.core;

import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.query.*;
import org.memoriadb.exception.*;
import org.memoriadb.util.*;


/**
 * The DBMode represents the two possibles modes for memoria: <b>class</b> and <b>data</b>. 
 * The class mode is the default mode in which memoria 
 * @author sandro
 *
 */
public enum DBMode {
  
  clazz {
    
    @Override
    public IObjectId addMemoriaClassIfNecessary(Object obj, final ObjectStore store) {
      
      if (obj.getClass().isArray()) {
        TypeInfo typeInfo = ReflectionUtil.getTypeInfo(obj.getClass());
        if(typeInfo.getComponentType()==Type.typeClass) addTypeHierarchy(store, typeInfo.getJavaClass());
        return store.getIdFactory().getArrayMemoriaClass();
      }
      
      return addTypeHierarchy(store, obj.getClass());
      
    }
    
    @Override
    public void checkCanInstantiateObject(IObjectRepo objectStore, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator) {
      IMemoriaClass memoriaClass = (IMemoriaClass) objectStore.getObject(memoriaClassId);
      
      Class<?> javaClass = ReflectionUtil.getClass(memoriaClass.getJavaClassName());
      if (ReflectionUtil.isNonStaticInnerClass(javaClass)) {
        throw new SchemaException("Can not save non-static inner classes " + javaClass);
      }
      
      memoriaClass.getHandler().checkCanInstantiateObject(memoriaClass.getJavaClassName(), defaultInstantiator);
    }
    
    @Override
    public IQueryStrategy instantiateQueryStrategy() {
      return new ClassModeQueryStrategy();
    }


    /**
     * @param store 
     * @return The id of the first (most derived) class in the hierarchy, because this is the
     * typeId of the object.
     * 
     * Idempotent, already stored classes are ignored.
     */
    private IObjectId addTypeHierarchy(ObjectStore store, Class<?> javaClass) {
      IMemoriaClassConfig classObject = store.internalGetMemoriaClass(javaClass.getName());

      // if the class is already in the store, all it's superclasses must also be known. Do nothing.
      if (classObject != null) {
        return store.getObjectId(classObject);
      }

      // add the current class and all its superclasses to the store
      classObject = MemoriaFieldClassFactory.createMetaClass(javaClass, store.getMemoriaFieldMetaClass());
      IObjectId result = store.internalSave(classObject);
      
      recursiveAddTypeHierarchy(store, javaClass, classObject);
      
      return result;
    }

    private void recursiveAddTypeHierarchy(ObjectStore store, Class<?> superClass, IMemoriaClassConfig subClassconfig) {
      Class<?> javaClass = superClass.getSuperclass();
      if(javaClass == null) return;

      // the super-class may already be there (bootstrapped, other hierarchy-branch)
      IMemoriaClassConfig classObject = store.internalGetMemoriaClass(javaClass.getName());
      if(classObject != null){
        subClassconfig.setSuperClass(classObject);
        return;
      }
      
      classObject = MemoriaFieldClassFactory.createMetaClass(javaClass, store.getMemoriaFieldMetaClass());
      store.internalSave(classObject);
      subClassconfig.setSuperClass(classObject);
      
      recursiveAddTypeHierarchy(store, javaClass, classObject);
    }
    
  },
  
  data {
    
    @Override
    public IObjectId addMemoriaClassIfNecessary(Object obj, ObjectStore store) {
      if (!(obj instanceof IDataObject)) throw new MemoriaException("We are in DBMode.data, but the added object is not of type IDataObject");
      
      IDataObject dataObject = (IDataObject) obj;
      if (!store.containsId(dataObject.getMemoriaClassId())) throw new MemoriaException("DataObject has no valid memoriaClassId");
      
      return dataObject.getMemoriaClassId();
    }
    
    @Override
    public void checkCanInstantiateObject(IObjectRepo objectStore, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator) {}
    
    @Override
    public IQueryStrategy instantiateQueryStrategy() {
      return new DataModeQueryStrategy();
    }
  };
  
  private final IQueryStrategy fQueryStrategy = instantiateQueryStrategy();

  /**
   * @return ObjectId of the MemoriaClass for the given obj 
   */
  public abstract IObjectId addMemoriaClassIfNecessary(Object obj, ObjectStore store);

  /**
   * Before an object is added to the ObjectRepository, it is checked for instantiability. 
   */
  public abstract void checkCanInstantiateObject(IObjectRepo objectStore, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator);
  
  public final IQueryStrategy getStrategy() {
    return fQueryStrategy;
  }
  
  protected abstract IQueryStrategy instantiateQueryStrategy();
  
}
