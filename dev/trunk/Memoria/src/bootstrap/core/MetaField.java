package bootstrap.core;

import java.io.DataOutput;
import java.lang.reflect.Field;

public final class MetaField {
  
  private int fId;
  private String fName;
  
  //TODO: Later, we can directly reference the enum. Memoria should this recognize and serialze the
  //ordinal value.
  private int fType;
  
  // the field used for reflection
  transient private Field fField;
  
  public MetaField(int id, String name, int ordinal) {
    fId = id;
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
    return fId;
  }

  public String getName() {
    return fName;
  }

  public void writeField(DataOutput stream, Object object, String className) throws Exception {
    stream.writeInt(getId());
    stream.writeInt(getType());
    FieldType.values()[getType()].writeValue(stream, object, getField(className));
  }
  
  
  private Field getField(String className) throws Exception {
    if(fField == null){
      try {
        fField = Class.forName(className).getDeclaredField(fName);
        fField.setAccessible(true);
      }catch(Exception e) {
        throw new Exception("No field " + fName + " on class " + className);
      }
    }
    
    return fField;
  }

}
