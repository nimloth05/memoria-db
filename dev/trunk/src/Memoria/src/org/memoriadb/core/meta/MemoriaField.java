package org.memoriadb.core.meta;

import java.lang.reflect.Field;

import org.memoriadb.exception.MemoriaException;


public final class MemoriaField {

  private final int fFieldId;
  private final String fName;

  // TODO: Later, we can directly reference the enum. Memoria should this recognize and serialze the
  // ordinal value.
  private final int fType;

  // the field used for reflection.
  private Field fField;
  private final Class<?> fClazz;

  public static MemoriaField create(int id, Field field) {
    MemoriaField result = new MemoriaField(id, field.getName(), Type.getType(field).ordinal(), field.getDeclaringClass());
    return result;
  }

  /**
   * 
   * @param id
   * @param name
   * @param ordinal
   * @param clazz - the java class where this field has been declared.
   */
  public MemoriaField(int id, String name, int ordinal, Class<?> clazz) {
    fFieldId = id;
    fType = ordinal;
    fName = name;
    fClazz = clazz;
  }

  public Type getFieldType() {
    return Type.values()[fType];
  }

  public int getId() {
    return fFieldId;
  }

  public Field getJavaField() {
    if (fField == null) {
      internalReadField();
    }

    return fField;
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

  private void internalReadField()  {
    try {
      fField = fClazz.getDeclaredField(fName);
      fField.setAccessible(true);
    } catch (NoSuchFieldException e) {
      throw new MemoriaException(e);
    }
  }

}
