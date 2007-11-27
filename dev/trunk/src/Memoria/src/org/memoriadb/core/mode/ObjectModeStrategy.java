package org.memoriadb.core.mode;

import org.memoriadb.core.*;
import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.handler.enu.EnumHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.*;
import org.memoriadb.util.*;

public class ObjectModeStrategy implements IModeStrategy {
  
  public static IObjectId addMemoriaClassIfNecessary(final TransactionHandler transactionHandler, Class<?> clazz) {
    if (clazz.isArray()) {
      ArrayTypeInfo arrayTypeInfo = ReflectionUtil.getComponentTypeInfo(clazz);
      if(!arrayTypeInfo.getComponentType().isPrimitive()) addTypeHierarchy(transactionHandler, arrayTypeInfo.getJavaClass());
      return transactionHandler.getDefaultIdProvider().getArrayMemoriaClass();
    }

    return addTypeHierarchy(transactionHandler, clazz);
  }
  
  public static void recursiveAddTypeHierarchy(TransactionHandler transactionHandler, Class<?> superClass, IMemoriaClassConfig subClassconfig) {
    Class<?> javaClass = superClass.getSuperclass();
    if(javaClass == null) return;

    // the super-class may already be there (bootstrapped or other hierarchy-branch)
    IMemoriaClassConfig classObject = transactionHandler.internalGetMemoriaClass(javaClass.getName());
    if(classObject != null){
      subClassconfig.setSuperClass(classObject);
      return;
    }
    
    classObject = MemoriaFieldClassFactory.createMetaClass(javaClass, transactionHandler.getDefaultIdProvider().getFieldMetaClass());
    transactionHandler.internalSave(classObject);
    subClassconfig.setSuperClass(classObject);
    
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
  }

  private static IObjectId addEnumClass(TransactionHandler transactionHandler, Class<?> javaClass) {
    IMemoriaClassConfig classObject;
    classObject = new HandlerbasedMemoriaClass(new EnumHandler(javaClass), transactionHandler.getDefaultIdProvider().getHandlerMetaClass());
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
    Class<?> enumClass = ReflectionUtil.getEnumClass(javaClass);
    if (enumClass != null) {
      javaClass = enumClass;
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
    classObject = MemoriaFieldClassFactory.createMetaClass(javaClass, transactionHandler.getDefaultIdProvider().getFieldMetaClass());
    IObjectId result = transactionHandler.internalSave(classObject);
    
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
    
    return result;
  }
  
  @Override
  public IObjectId addMemoriaClassIfNecessary(final TransactionHandler transactionHandler, Object obj) {
    return addMemoriaClassIfNecessary(transactionHandler, obj.getClass());
  }

  @Override
  public void checkCanInstantiateObject(ITransactionHandler transactionHandler, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator) {
    IMemoriaClass memoriaClass = (IMemoriaClass) transactionHandler.getObject(memoriaClassId);
    memoriaClass.getHandler().checkCanInstantiateObject(memoriaClass.getJavaClassName(), defaultInstantiator);
  }
  

  @Override
  public void checkObject(Object obj) {
    if(obj instanceof IDataObject && !(obj instanceof IMemoriaClass)) throw new MemoriaException("IDataObjects are for data-mode only: " + obj);
    
    if (ReflectionUtil.isNonStaticInnerClass(obj.getClass())) {
      throw new SchemaException("cannot save non-static inner classes " + obj.getClass());
    }
    
    if(Type.isPrimitive(obj)) throw new MemoriaException("cannot save primitive"); 
  }

  @Override
  public boolean isDataMode() {
    return false;
  }
  
  
}
