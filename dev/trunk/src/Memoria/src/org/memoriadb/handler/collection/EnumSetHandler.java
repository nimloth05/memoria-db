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

package org.memoriadb.handler.collection;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.collection.CollectionHandler.SetHandler;
import org.memoriadb.id.IObjectId;

import java.io.*;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Sandro
 */
public class EnumSetHandler extends SetHandler {

  public static final String sRegularEnumSet = "java.util.RegularEnumSet";
  public static final String sJumboEnumSet = "java.util.JumboEnumSet";
  private static final String FIElD_NAME_FOR_ENUM_TYPE_INFO = "elementType";

  private IMemoriaClass fEnumClass;
  
  public EnumSetHandler(String name) {
    super(name);
  }

  @Override
  public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
    IObjectId enumClassId = context.readObjectId(input);
    fEnumClass = (IMemoriaClass) context.getExistingObject(enumClassId);
    return super.deserialize(input, context, typeId);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    EnumSet<?> enumSet = (EnumSet<?>) obj;
    Class<Enum> enumType = getEnumType(enumSet);
    IObjectId memoriaClassId = context.getMemoriaClassId(ReflectionUtil.getCorrectEnumClass(enumType).getName());
    memoriaClassId.writeTo(output);

    super.serialize(obj, output, context);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Set<Object> createCollectionForObjectMode() {
    Class<Enum> enumClass = (Class<Enum>) ReflectionUtil.getClass(fEnumClass.getJavaClassName());
    Set<Enum> enumSet = EnumSet.noneOf(enumClass);
    return convert(enumSet);
  }
  
  @SuppressWarnings("unchecked")
  private <T> Set<T> convert(Set<Enum> enumSet) {
    return (Set<T>) enumSet;
  }

  @SuppressWarnings("unchecked")
  private Class<Enum> getEnumType(EnumSet<?> enumSet) {
    try {
      return (Class<Enum>) ReflectionUtil.getFieldValue(enumSet, FIElD_NAME_FOR_ENUM_TYPE_INFO);
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

}
