package org.memoriadb.core;

import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.query.*;
import org.memoriadb.exception.*;
import org.memoriadb.util.ReflectionUtil;


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
      Class<?> klass = obj.getClass();

      // if obj is an array, the metaClass of the componentType is added.
      // The MetaClass for the array is generic and bootstrapped

      return new InheritanceTraverser(klass) {

        private IMemoriaClassConfig fChildClass = null;
        private IObjectId fObjectMemoriaClass;

        @Override
        public void handle(Class<?> superClass) {
          if (superClass.isArray()) {
            superClass = ReflectionUtil.getTypeInfo(superClass).getJavaClass(); 
            if (fObjectMemoriaClass == null) fObjectMemoriaClass = store.getArrayMetaClass();
          }
          
          IMemoriaClassConfig classObject = store.internalGetMemoriaClass(superClass.getName());

          if (classObject != null) {
            if (fChildClass != null) fChildClass.setSuperClass(classObject);
            if (fObjectMemoriaClass == null) fObjectMemoriaClass = store.getObjectId(classObject);
            abort();
            return;
          }

          classObject = MemoriaFieldClassFactory.createMetaClass(superClass, store.getMemoriaFieldMetaClass());
          if (fChildClass != null) fChildClass.setSuperClass(classObject);
          fChildClass = classObject;
          
          IObjectId id = store.internalSave(classObject);
          if (fObjectMemoriaClass == null) fObjectMemoriaClass = id;
        }
      }.fObjectMemoriaClass;
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
