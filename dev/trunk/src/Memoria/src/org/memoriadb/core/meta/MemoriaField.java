package org.memoriadb.core.meta;

import java.lang.reflect.Field;


public final class MemoriaField {

  private final int fFieldId;
  private final String fName;
  private final Type fType;

  public static MemoriaField create(int id, Field field) {
    MemoriaField result = new MemoriaField(id, field.getName(), Type.getType(field));
    return result;
  }

  /**
   * 
   * @param id
   * @param name
   * @param ordinal
   * 
   */
  public MemoriaField(int id, String name, Type type) {
    fFieldId = id;
    fType = type;
    fName = name;
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

  @Override
  public String toString() {
    return "FieldName: " + fName;
  }

}
