package org.memoriadb.util;

import org.memoriadb.core.meta.Type;

public class TypeInfo {

  private final Type fComponentType;
  private final int fDimension;

  // if the ComponentType is clazz, the java class must be saved.
  private final String fClassName;

  /**
   * @param componentType
   * @param dimension
   * @param className
   *          Only valid when the given componentType is {@link Type#typeClass}.
   */
  public TypeInfo(Type componentType, int dimension, String className) {
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
}
