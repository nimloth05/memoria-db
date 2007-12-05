package org.memoriadb.handler.collection;

import java.io.*;
import java.util.*;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.collection.CollectionHandler.SetHandler;
import org.memoriadb.id.IObjectId;

public class EnumSetHandler extends SetHandler {

  public static final String sRegularEnumSet = "java.util.RegularEnumSet";
  public static final String sJumboEnumSet = "java.util.JumboEnumSet";
  private static final String FIElD_NAME_FOR_ENUM_TYPE_INFO = "elementType";

  private IMemoriaClass fEnumClass;
  
  public EnumSetHandler(String name) {
    super(name);
  }

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception {
    IObjectId enumClassId = context.readObjectId(input);
    fEnumClass = (IMemoriaClass) context.getExistingObject(enumClassId);
    return super.deserialize(input, context, typeId);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void serialize(Object obj, DataOutput output, ISerializeContext context) throws Exception {
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
    EnumSet<?> noneOf = EnumSet.noneOf(enumClass);
    return (Set<Object>) noneOf;
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
