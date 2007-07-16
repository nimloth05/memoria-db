package bootstrap.core;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public enum FieldType {

  booleanPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeBoolean(field.getBoolean(object));
    }

  },
  charPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeChar(field.getChar(object));
    }

  },
  typePrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeByte(field.getByte(object));
    }

  },
  shortPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeShort(field.getShort(object));
    }

  },
  integerPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
      super.writeValue(stream, object, field);
      stream.writeInt(field.getInt(object));
    }

  },
  longPrimitive {

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
  doublePrimitive {

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

    
    //TODO Test if this works for non-primitives
    
    result.put(Boolean.class, booleanPrimitive);
    result.put(Boolean.TYPE, booleanPrimitive);

    result.put(Character.class, charPrimitive);
    result.put(Character.TYPE, charPrimitive);

    result.put(Byte.class, typePrimitive);
    result.put(Byte.TYPE, typePrimitive);

    result.put(Short.class, shortPrimitive);
    result.put(Short.TYPE, shortPrimitive);

    result.put(Integer.class, integerPrimitive);
    result.put(Integer.TYPE, integerPrimitive);

    result.put(Long.class, longPrimitive);
    result.put(Long.TYPE, longPrimitive);

    result.put(Float.class, flot);
    result.put(Float.TYPE, flot);

    result.put(Double.class, doublePrimitive);
    result.put(Double.TYPE, doublePrimitive);

    result.put(String.class, string);

    return result;
  }

  public void writeValue(DataOutput stream, Object object, Field field) throws Exception {
    field.setAccessible(true);
  }

}
