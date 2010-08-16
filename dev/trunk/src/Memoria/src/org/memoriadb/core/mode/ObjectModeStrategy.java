/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.core.mode;

import org.memoriadb.core.IObjectRepository;
import org.memoriadb.core.TransactionHandler;
import org.memoriadb.core.TypeHierarchyBuilder;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.meta.Type;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

/**
 * @author Sandro
 */
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
  public Object createEnum(Enum<? extends Enum<?>> current, IObjectId memoriaClassId) {
    return current;
  }

  @Override
  public IMemoriaClass getMemoriaClass(Object object, IObjectRepository objectRepository) {
    IMemoriaClass result = objectRepository.getMemoriaClass(object);
    if(result != null) return result;
    
    // value-objects
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
