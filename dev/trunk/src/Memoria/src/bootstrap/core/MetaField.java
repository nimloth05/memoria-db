package bootstrap.core;

import java.io.*;
import java.lang.reflect.Field;

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

  public Field getJavaField(Object object) throws Exception {
    if (fField == null) {
      fField = object.getClass().getDeclaredField(fName);
      fField.setAccessible(true);
    }

    return fField;
  }

  public FieldType getFieldType() {
    return FieldType.values()[fType];
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
  
  public void readField(DataInput stream, Object object, IReaderContext context) throws Exception {
    FieldType.values()[getType()].readValue(stream, object, getJavaField(object), context);
  }
  
  @Override
  public String toString() {
    return "FieldName: "+fName;
  }

  public void writeField(DataOutput stream, Object object, IContext context) throws Exception {
    stream.writeInt(getId());
    FieldType.values()[getType()].writeValue(stream, object, getJavaField(object), context);
  }

}
