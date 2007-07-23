package bootstrap.core;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public enum FieldType {

  booleanPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      stream.writeBoolean((Boolean) field.get(object));
    }

    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      field.set(object, stream.readBoolean());
    }
    
  },
  charPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      stream.writeChar((Character)field.get(object));
    }
    
    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      field.set(object, stream.readChar());
    }
    

  },
  bytePrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      stream.writeByte((Byte)field.get(object));
    }
    
    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      field.set(object, stream.readByte());
    }

  },
  shortPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      stream.writeShort((Short)field.get(object));
    }
    
    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      field.set(object, stream.readShort());
    }

  },
  integerPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      stream.writeInt((Integer)field.get(object));
    }
    
    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      field.set(object, stream.readInt());
    }

  },
  longPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      stream.writeLong((Long)field.get(object));
    }
    
    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      field.set(object, stream.readLong());
    }

  },
  floatPrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      stream.writeFloat((Float)field.get(object));
    }
    
    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      field.set(object, stream.readFloat());
    }

  },
  doublePrimitive {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      stream.writeDouble((Double)field.get(object));
    }
    
    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      field.set(object, stream.readDouble());
    }

  },
  string {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      Object rawValue = field.get(object);
      if (rawValue != null) {
        stream.writeUTF(rawValue.toString());
      } else {
        stream.writeUTF(""); //$NON-NLS-1$
      }
    }
    
    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      field.set(object, stream.readUTF());
    }

  },
  clazz {

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception {
      Object referencee = field.get(object);
      long objectId = context.register(referencee);
      stream.writeLong(objectId);
    }
    
    @Override
    public void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception {
      long targetId = stream.readLong();
      context.objectToBind(object, field, targetId);
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

    result.put(Byte.class, bytePrimitive);
    result.put(Byte.TYPE, bytePrimitive);

    result.put(Short.class, shortPrimitive);
    result.put(Short.TYPE, shortPrimitive);

    result.put(Integer.class, integerPrimitive);
    result.put(Integer.TYPE, integerPrimitive);

    result.put(Long.class, longPrimitive);
    result.put(Long.TYPE, longPrimitive);

    result.put(Float.class, floatPrimitive);
    result.put(Float.TYPE, floatPrimitive);

    result.put(Double.class, doublePrimitive);
    result.put(Double.TYPE, doublePrimitive);

    result.put(String.class, string);

    return result;
  }

  public abstract void writeValue(DataOutput stream, Object object, Field field, IContext context) throws Exception;
  

  public abstract void readValue(DataInput stream, Object object, Field field, IContext context) throws Exception;

}
