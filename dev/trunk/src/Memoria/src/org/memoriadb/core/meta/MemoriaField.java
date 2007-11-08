package org.memoriadb.core.meta;

import java.lang.reflect.Field;


public final class MemoriaField {

  private final int fFieldId;
  private final String fName;

  // FIXME: Es gibt später die Möglichkeit, Enums direkt zu speichern, ohne das ordinal.
  private final int fType;

  public static MemoriaField create(int id, Field field) {
    MemoriaField result = new MemoriaField(id, field.getName(), Type.getType(field).ordinal());
    return result;
  }

  /**
   * 
   * @param id
   * @param name
   * @param ordinal
   * @param clazz - the java class where this field has been declared.
   */
  public MemoriaField(int id, String name, int ordinal) {
    fFieldId = id;
    fType = ordinal;
    fName = name;
  }

  public Type getFieldType() {
    return Type.values()[fType];
  }

  public int getId() {
    return fFieldId;
  }

  public String getName() {
    return fName;
  }
  
  public int getType() {
    return fType;
  }

  @Override
  public String toString() {
    return "FieldName: "+fName;
  }

}
