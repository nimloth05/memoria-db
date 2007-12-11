package org.memoriadb.core.util;

import java.io.*;

import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.handler.array.*;
import org.memoriadb.id.IObjectId;

// FIXME allenfalls 2 Klassen für primitive und \primitive
public class ArrayTypeInfo {

  private final Type fComponentType;
  private final int fDimension;

  // if the ComponentType is clazz, the java class must be saved.
  private final String fClassName;

  public static IArray readTypeInfo(DataInputStream input, IReaderContext context) throws IOException {
    int dimension = input.readInt();
    int length = input.readInt();
    Type componentType = Type.values()[input.readByte()];

    return instantiateArray(input, context, dimension, length, componentType);
  }

  private static IArray instantiateArray(DataInputStream input, IReaderContext context, int dimension, int length, Type componentType)
      throws IOException {

    if (componentType.isPrimitive()) {
      // name is given only for debug-reasons
      ArrayTypeInfo arrayTypeInfo = new ArrayTypeInfo(componentType, dimension, componentType.name());

      return instantiateArray(context, length, arrayTypeInfo);
    }

    // read MemoriaClass of componentType
    IObjectId memoriaClassId = context.readObjectId(input);
    IMemoriaClassConfig memoriaClass = (IMemoriaClassConfig) context.getExistingObject(memoriaClassId);

    ArrayTypeInfo arrayTypeInfo = new ArrayTypeInfo(componentType, dimension, memoriaClass.getJavaClassName());

    return instantiateArray(context, length, arrayTypeInfo);
  }

  private static IArray instantiateArray(IReaderContext context, int length, ArrayTypeInfo arrayTypeInfo) {
    if (context.isInDataMode()) return new DataArray(context.getArrayMemoriaClass(), arrayTypeInfo, length);
    return new ObjectArray(context.getArrayMemoriaClass(), arrayTypeInfo, length);
  }

  /**
   * @param componentType
   * @param dimension
   * @param className
   *          Only valid when the given componentType is {@link Type#typeClass}.
   */
  public ArrayTypeInfo(Type componentType, int dimension, String className) {
    fComponentType = componentType;
    fDimension = dimension;
    fClassName = className;
  }

  public String getClassName() {
    return fClassName;
  }

  public Type getComponentType() {
    return fComponentType;
  }

  public int getDimension() {
    return fDimension;
  }

  public Class<?> getJavaClass() {
    if (fComponentType.isPrimitive()) return fComponentType.getClassLiteral();
    return ReflectionUtil.getClass(getClassName());
  }

  public boolean isPrimitive() {
    return fComponentType.isPrimitive();
  }

  public void writeTypeInfo(int length, DataOutput output, IWriterContext context) throws IOException {

    output.writeInt(getDimension());
    output.writeInt(length);

    // write type-ordinal
    output.writeByte(getComponentType().ordinal());

    // if componentType is a class, write MemoriaClassId
    if (getComponentType() == Type.typeClass) {
      context.getMemoriaClassId(getClassName()).writeTo(output);
    }
  }
}
