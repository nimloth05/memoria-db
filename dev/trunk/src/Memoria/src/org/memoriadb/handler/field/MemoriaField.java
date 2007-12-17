package org.memoriadb.handler.field;

import java.lang.reflect.Field;

import org.memoriadb.core.meta.Type;
import org.memoriadb.core.util.ReflectionUtil;


public final class MemoriaField {

  private final int fFieldId;
  private final String fName;
  private final Type fType;
  private final boolean fIsWeakRef;

  public static MemoriaField create(int id, Field field) {
    MemoriaField result = new MemoriaField(id, field.getName(), Type.getType(field), ReflectionUtil.hasWeakRefAnnotation(field));
    return result;
  }

  /**
   * @param id
   * @param name
   * @param ordinal
   */
  public MemoriaField(int id, String name, Type type, boolean isWeakRef) {
    fFieldId = id;
    fType = type;
    fName = name;
    fIsWeakRef = isWeakRef;
  }

  public Type getFieldType() {
    return fType;
  }

  public int getId() {
    return fFieldId;
  }

  public String getName() {
    return fName;
  }
  
  public Type getType() {
    return fType;
  }

  public boolean isWeakRef() {
    return fIsWeakRef;
  }

  @Override
  public String toString() {
    return "FieldName: " + fName;
  }

}
