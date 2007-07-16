package bootstrap.core;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public enum FieldType {

  bool {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeBoolean(field.getBoolean(object));
    }

  },
  chr {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeChar(field.getChar(object));
    }

  },
  bte {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeByte(field.getByte(object));
    }

  },
  shrt {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeShort(field.getShort(object));
    }

  },
  integer {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeInt(field.getInt(object));
    }

  },
  lng {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeLong(field.getLong(object));
    }

  },
  flot {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeFloat(field.getFloat(object));
    }

  },
  duble {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeDouble(field.getDouble(object));
    }

  },
  string {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      Object rawValue = field.get(object);
      if (rawValue != null) {
        stream.writeUTF(rawValue.toString());
      } else {
        stream.writeUTF(""); //$NON-NLS-1$
      }
    }

  },
  clazz {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      //System.out.println("try to write field: " + field + " of class" + field.getDeclaringClass());
    }

  };

  private static final Map<Class<?>, FieldType> sPrimitiveTypeMap = createTypeMap();

  public static FieldType getType(Field field) {
    FieldType type = sPrimitiveTypeMap.get(field.getType());
    if (type == null) return clazz;
    return type;
  }

  private static Map<Class<?>, FieldType> createTypeMap() {
    Map<Class<?>, FieldType> result = new HashMap<Class<?>, FieldType>();

    result.put(Boolean.class, bool);
    result.put(Boolean.TYPE, bool);

    result.put(Character.class, chr);
    result.put(Character.TYPE, chr);

    result.put(Byte.class, bte);
    result.put(Byte.TYPE, bte);

    result.put(Short.class, shrt);
    result.put(Short.TYPE, shrt);

    result.put(Integer.class, integer);
    result.put(Integer.TYPE, integer);

    result.put(Long.class, lng);
    result.put(Long.TYPE, lng);

    result.put(Float.class, flot);
    result.put(Float.TYPE, flot);

    result.put(Double.class, duble);
    result.put(Double.TYPE, duble);

    result.put(String.class, string);

    return result;
  }

  public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
    field.setAccessible(true);
  }

}
