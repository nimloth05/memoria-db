package org.memoriadb.core.mode;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.enu.EnumHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.*;
import org.memoriadb.util.*;

public class ObjectModeStrategy implements IModeStrategy {
  
  @Override
  public IObjectId addMemoriaClassIfNecessary(final TransactionHandler transactionHandler, Object obj) {
    if (obj.getClass().isArray()) {
      TypeInfo typeInfo = ReflectionUtil.getComponentTypeInfo(obj.getClass());
      if(typeInfo.getComponentType() == Type.typeClass) addTypeHierarchy(transactionHandler, typeInfo.getJavaClass());
      return transactionHandler.getIdFactory().getArrayMemoriaClass();
    }
    
    return addTypeHierarchy(transactionHandler, obj.getClass());
  }

  @Override
  public void checkCanInstantiateObject(ITransactionHandler transactionHandler, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator) {
    IMemoriaClass memoriaClass = (IMemoriaClass) transactionHandler.getObject(memoriaClassId);
    memoriaClass.getHandler().checkCanInstantiateObject(memoriaClass.getJavaClassName(), defaultInstantiator);
  }
  
  @Override
  public boolean isDataMode() {
    return false;
  }

  private IObjectId addEnumClass(TransactionHandler transactionHandler, Class<?> javaClass) {
    IMemoriaClassConfig classObject;
    classObject = new MemoriaHandlerClass(new EnumHandler(javaClass), transactionHandler.getIdFactory().getHandlerMetaClass());
    IObjectId result = transactionHandler.internalSave(classObject);
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
    return result;
  }

  /**
   * 
   * @param transactionHandler 
   * 
   * @return The id of the classObject for the given javaClass.  
   * 
   * Idempotent, already stored classes are ignored.
   */
  private IObjectId addTypeHierarchy(TransactionHandler transactionHandler, Class<?> javaClass) {
    if (javaClass.getSuperclass() != null && javaClass.getSuperclass().isEnum()) {
      javaClass = javaClass.getSuperclass();
    }
    
    IMemoriaClassConfig classObject = transactionHandler.internalGetMemoriaClass(javaClass.getName());

    // if the class is already in the store, all it's superclasses must also be known. Do nothing.
    if (classObject != null) {
      return transactionHandler.getObjectId(classObject);
    }
    
    if (javaClass.isEnum()) {
      return addEnumClass(transactionHandler, javaClass);
    }
    
    
    // add the current class and all its superclasses to the store
    classObject = MemoriaFieldClassFactory.createMetaClass(javaClass, transactionHandler.getMemoriaFieldMetaClass());
    IObjectId result = transactionHandler.internalSave(classObject);
    
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
    
    return result;
  }
  

  private void recursiveAddTypeHierarchy(TransactionHandler transactionHandler, Class<?> superClass, IMemoriaClassConfig subClassconfig) {
    Class<?> javaClass = superClass.getSuperclass();
    if(javaClass == null) return;

    // the super-class may already be there (bootstrapped or other hierarchy-branch)
    IMemoriaClassConfig classObject = transactionHandler.internalGetMemoriaClass(javaClass.getName());
    if(classObject != null){
      subClassconfig.setSuperClass(classObject);
      return;
    }
    
    classObject = MemoriaFieldClassFactory.createMetaClass(javaClass, transactionHandler.getMemoriaFieldMetaClass());
    transactionHandler.internalSave(classObject);
    subClassconfig.setSuperClass(classObject);
    
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
  }
  
  
}
