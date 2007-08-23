package org.memoriadb.core.meta;

import java.lang.reflect.Field;

import org.memoriadb.exception.MemoriaException;


public final class MetaField {

  private final int fFieldId;
  private final String fName;

  // TODO: Later, we can directly reference the enum. Memoria should this recognize and serialze the
  // ordinal value.
  private final int fType;

  // the field used for reflection.
  private Field fField;

  public static MetaField create(int id, Field field) {
    MetaField result = new MetaField(id, field.getName(), FieldType.getType(field).ordinal());
    return result;
  }

  public MetaField(int id, String name, int ordinal) {
    fFieldId = id;
    fType = ordinal;
    fName = name;
  }

  public FieldType getFieldType() {
    return FieldType.values()[fType];
  }

  public int getId() {
    return fFieldId;
  }

  public Field getJavaField(Object object) {
    if (fField == null) {
      internalReadField(object);
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

  private void internalReadField(Object object)  {
    try {
      fField = object.getClass().getDeclaredField(fName);
      fField.setAccessible(true);
    } catch (NoSuchFieldException e) {
      throw new MemoriaException(e);
    }
  }

}
