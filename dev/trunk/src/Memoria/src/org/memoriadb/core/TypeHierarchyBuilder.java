package org.memoriadb.core;

import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.core.meta.MemoriaClass;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.enu.EnumHandler;
import org.memoriadb.handler.field.ReflectionHandlerFactory;
import org.memoriadb.id.IObjectId;

public final class TypeHierarchyBuilder {

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
    
    classObject = ReflectionHandlerFactory.createNewType(javaClass, transactionHandler.getDefaultIdProvider().getFieldMetaClass());
    transactionHandler.internalSave(classObject);
    subClassconfig.setSuperClass(classObject);
    
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
  }

  private static IObjectId addEnumClass(TransactionHandler transactionHandler, Class<?> javaClass, IModeStrategy modeStrategy) {
    IMemoriaClassConfig classObject;
    classObject = new MemoriaClass(new EnumHandler(javaClass), transactionHandler.getDefaultIdProvider().getHandlerMetaClass(), false);
    IObjectId result = transactionHandler.internalSave(classObject);
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
    
    // enum class added, add enum-instances
    for(Enum<?> current: (Enum<?>[]) javaClass.getEnumConstants()) {
      //result is the objectId for the memoriaClass which represents the given enum-class. 04.04.2009, so
      transactionHandler.internalAddObject(modeStrategy.createEnum(current, result), result);
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
    classObject = ReflectionHandlerFactory.createNewType(javaClass, transactionHandler.getDefaultIdProvider().getFieldMetaClass());
    IObjectId result = transactionHandler.internalSave(classObject);
    
    recursiveAddTypeHierarchy(transactionHandler, javaClass, classObject);
    
    return result;
  }
  
  private TypeHierarchyBuilder() {}
  
}
