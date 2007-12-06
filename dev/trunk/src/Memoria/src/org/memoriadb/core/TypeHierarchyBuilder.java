package org.memoriadb.core;

import org.memoriadb.core.meta.*;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.*;
import org.memoriadb.handler.enu.EnumHandler;
import org.memoriadb.id.IObjectId;

public class TypeHierarchyBuilder {

  public static IObjectId addMemoriaClassIfNecessary(final TransactionHandler transactionHandler, Class<?> clazz, IModeStrategy modeStrategy) {
    if (clazz.isArray()) {
      ArrayTypeInfo arrayTypeInfo = ReflectionUtil.getComponentTypeInfo(clazz);
      if(!arrayTypeInfo.getComponentType().isPrimitive()) addTypeHierarchy(transactionHandler, arrayTypeInfo.getJavaClass(), modeStrategy);
      return transactionHandler.getDefaultIdProvider().getArrayMemoriaClass();
    }

    return addTypeHierarchy(transactionHandler, clazz, modeStrategy);
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
    
    classObject = FieldbasedMemoriaClassFactory.createMetaClass(javaClass, transactionHandler.getDefaultIdProvider().getFieldMetaClass());
    transactionHandler.internalSave(classObject);
    subClassconfig.setSuperClass(classObject);
    
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
  }

  private static IObjectId addEnumClass(TransactionHandler transactionHandler, Class<?> javaClass, IModeStrategy modeStrategy) {
    IMemoriaClassConfig classObject;
    classObject = new HandlerbasedMemoriaClass(new EnumHandler(javaClass), transactionHandler.getDefaultIdProvider().getHandlerMetaClass(), false);
    IObjectId result = transactionHandler.internalSave(classObject);
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
    
    // enum class added, add enum-instances
    for(Enum<?> current: (Enum<?>[]) javaClass.getEnumConstants()) {
      transactionHandler.internalAddObject(modeStrategy.createEnum(current, result));
    }
    
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
  private static IObjectId addTypeHierarchy(TransactionHandler transactionHandler, Class<?> javaClass, IModeStrategy modeStrategy) {
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
      return addEnumClass(transactionHandler, javaClass, modeStrategy);
    }
    
    // add the current class and all its superclasses to the store
    classObject = FieldbasedMemoriaClassFactory.createMetaClass(javaClass, transactionHandler.getDefaultIdProvider().getFieldMetaClass());
    IObjectId result = transactionHandler.internalSave(classObject);
    
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
    
    return result;
  }
  
}
