package org.memoriadb.core.mode;

import org.memoriadb.core.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.SchemaException;
import org.memoriadb.util.*;

public class ObjectModeStrategy implements IModeStrategy {
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
  public void checkCanInstantiateObject(ITrxHandler trxHandler, IObjectId memoriaClassId, IDefaultInstantiator defaultInstantiator) {
    IMemoriaClass memoriaClass = (IMemoriaClass) trxHandler.getObject(memoriaClassId);
    
    Class<?> javaClass = ReflectionUtil.getClass(memoriaClass.getJavaClassName());
    if (ReflectionUtil.isNonStaticInnerClass(javaClass)) {
      throw new SchemaException("Can not save non-static inner classes " + javaClass);
    }
    
    memoriaClass.getHandler().checkCanInstantiateObject(memoriaClass.getJavaClassName(), defaultInstantiator);
  }
  
  @Override
  public boolean isInDataMode() {
    return false;
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