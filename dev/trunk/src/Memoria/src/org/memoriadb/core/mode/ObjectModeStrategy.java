package org.memoriadb.core.mode;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.enu.EnumHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.*;
import org.memoriadb.util.*;

public class ObjectModeStrategy implements IModeStrategy {
  
  public static IObjectId addMemoriaClassIfNecessary(final TransactionHandler transactionHandler, Class<?> clazz) {
    if (clazz.isArray()) {
      ArrayTypeInfo arrayTypeInfo = ReflectionUtil.getComponentTypeInfo(clazz);
      if(!arrayTypeInfo.getComponentType().isPrimitive()) addTypeHierarchy(transactionHandler, arrayTypeInfo.getJavaClass());
      return transactionHandler.getIdFactory().getArrayMemoriaClass();
    }

    return addTypeHierarchy(transactionHandler, clazz);
  }
  
  private static IObjectId addEnumClass(TransactionHandler transactionHandler, Class<?> javaClass) {
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
  private static IObjectId addTypeHierarchy(TransactionHandler transactionHandler, Class<?> javaClass) {
    if (javaClass.getSuperclass() != null && javaClass.getSuperclass().isEnum()) {
      javaClass = javaClass.getSuperclass();
    }
    
    IMemoriaClassConfig classObject = transactionHandler.internalGetMemoriaClass(javaClass.getName());

    // if the class is already in the store, all it's superclasses must also be known. Do nothing.
    if (classObject != null) {
      return transactionHandler.getExistingId(classObject);
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

  private static void recursiveAddTypeHierarchy(TransactionHandler transactionHandler, Class<?> superClass, IMemoriaClassConfig subClassconfig) {
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
  
  @Override
  public IObjectId addMemoriaClassIfNecessary(final TransactionHandler transactionHandler, Object obj) {
    
    if (obj.getClass().isArray()) {
      ArrayTypeInfo arrayTypeInfo = ReflectionUtil.getComponentTypeInfo(obj.getClass());
      if(arrayTypeInfo.getComponentType()==Type.typeClass) addTypeHierarchy(transactionHandler, arrayTypeInfo.getJavaClass());
      return transactionHandler.getIdFactory().getArrayMemoriaClass();
    }
    
    if (obj.getClass().isEnum()) {
      Class<?> enumClass = obj.getClass();
      if (enumClass.getSuperclass().isEnum()) enumClass = enumClass.getSuperclass();
      return addTypeHierarchy(transactionHandler, enumClass);
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
  
  
}
