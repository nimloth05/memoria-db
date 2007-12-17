package org.memoriadb.core.mode;

import org.memoriadb.core.*;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public final class ObjectModeStrategy implements IModeStrategy {
  
  @Override
  public IObjectId addMemoriaClassIfNecessary(final TransactionHandler transactionHandler, Object obj) {
    return TypeHierarchyBuilder.addMemoriaClassIfNecessary(transactionHandler, obj.getClass(), this);
  }

  @Override
  public void checkCanInstantiateObject(TransactionHandler transactionHandler, IObjectId memoriaClassId, IInstantiator instantiator) {
    IMemoriaClass memoriaClass = (IMemoriaClass) transactionHandler.getObject(memoriaClassId);
    memoriaClass.getHandler().checkCanInstantiateObject(memoriaClass.getJavaClassName(), instantiator);
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
  public Object createEnum(Enum<?> current, IObjectId memoriaClassId) {
    return current;
  }

  @Override
  public IMemoriaClass getMemoriaClass(Object object, IObjectRepository objectRepository) {
    IMemoriaClass memoriaClass = objectRepository.getMemoriaClass(object);
    if (memoriaClass != null) return memoriaClass;
    return objectRepository.getMemoriaClass(object.getClass().getName());
  }

  @Override
  public boolean isDataMode() {
    return false;
  }

  @Override
  public boolean isEnum(Object object) {
    return ReflectionUtil.isEnum(object.getClass());
  }
  
  
}
