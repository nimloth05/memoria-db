package org.memoriadb.core.handler.collection;

import java.io.*;
import java.util.*;

import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.ReflectionUtil;

public class EnumSetHandler extends CollectionHandler {

  private static final String FIElD_NAME_FOR_ENUM_TYPE_INFO = "elementType";
  public static final String sRegularEnumSet = "java.util.RegularEnumSet";
  public static final String sJumboEnumSet = "java.util.JumboEnumSet";

  private IMemoriaClass fEnumClass;

  private final String fClassName;

  public EnumSetHandler(String className) {
    fClassName = className;
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception {
    IObjectId enumClassId = context.readObjectId(input);
    fEnumClass = (IMemoriaClass) context.getExistingObject(enumClassId);
    return super.deserialize(input, context, typeId);
  }

  @Override
  public String getClassName() {
    return fClassName;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    EnumSet<?> enumSet = (EnumSet<?>) obj;
    Class<Enum> enumType = getEnumType(enumSet);
    IObjectId memoriaClassId = context.getMemoriaClassId(ReflectionUtil.getCorrectEnumClass(enumType).getName());
    memoriaClassId.writeTo(output);

    super.serialize(obj, output, context);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Collection<Object> createCollection() {
    Class<Enum> enumClass = (Class<Enum>) ReflectionUtil.getClass(fEnumClass.getJavaClassName());
    EnumSet<?> noneOf = EnumSet.noneOf(enumClass);
    return (Collection<Object>) noneOf;
  }

  @Override
  protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
    return new SetDataObject((Set<Object>) collection, typeId);
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
