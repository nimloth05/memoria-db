package bootstrap.core;

import java.io.*;
import java.lang.reflect.Field;

public final class MetaField {

  private int fFieldId;
  private String fName;

  // TODO: Later, we can directly reference the enum. Memoria should this recognize and serialze the
  // ordinal value.
  private int fType;

  // the field used for reflection.
  private Field fField;

  public MetaField(int id, String name, int ordinal) {
    fFieldId = id;
    fType = ordinal;
    fName = name;
  }

  public static MetaField create(int id, Field field) {
    MetaField result = new MetaField(id, field.getName(), FieldType.getType(field).ordinal());
    return result;
  }

  public int getType() {
    return fType;
  }

  public int getId() {
    return fFieldId;
  }

  public String getName() {
    return fName;
  }

  public void writeField(DataOutput stream, Object object, IContext context) throws Exception {
    stream.writeInt(getId());
    FieldType.values()[getType()].writeValue(stream, object, getField(object), context);
  }
  
  public void readField(DataInput stream, Object object, IContext context) throws Exception {
    FieldType.values()[getType()].readValue(stream, object, getField(object), context);
  }

  private Field getField(Object object) throws Exception {
    if (fField == null) {
      fField = object.getClass().getDeclaredField(fName);
      fField.setAccessible(true);
    }

    return fField;
  }
  
  @Override
  public String toString() {
    return "FieldName: "+fName;
  }

}
