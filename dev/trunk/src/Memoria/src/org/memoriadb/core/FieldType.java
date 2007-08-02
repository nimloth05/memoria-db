package org.memoriadb.core;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import org.memoriadb.exception.MemoriaException;


public enum FieldType {

  booleanPrimitive {

    @Override
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      field.set(object, stream.readBoolean());
    }

    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
      stream.writeBoolean((Boolean) field.get(object));
    }
    
  },
  charPrimitive {

    @Override
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      field.set(object, stream.readChar());
    }
    
    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
      stream.writeChar((Character)field.get(object));
    }
    

  },
  bytePrimitive {

    @Override
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      field.set(object, stream.readByte());
    }
    
    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
      stream.writeByte((Byte)field.get(object));
    }

  },
  shortPrimitive {

    @Override
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      field.set(object, stream.readShort());
    }
    
    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
      stream.writeShort((Short)field.get(object));
    }

  },
  integerPrimitive {

    @Override
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      field.set(object, stream.readInt());
    }
    
    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
      stream.writeInt((Integer)field.get(object));
    }

  },
  longPrimitive {

    @Override
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      field.set(object, stream.readLong());
    }
    
    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
      stream.writeLong((Long)field.get(object));
    }

  },
  floatPrimitive {

    @Override
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      field.set(object, stream.readFloat());
    }
    
    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
      stream.writeFloat((Float)field.get(object));
    }

  },
  doublePrimitive {

    @Override
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      field.set(object, stream.readDouble());
    }
    
    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
      stream.writeDouble((Double)field.get(object));
    }

  },
  string {

    @Override
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      field.set(object, stream.readUTF());
    }
    
    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
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
    public void internalReadValue(DataInput stream, Object object, Field field, IReaderContext context) throws Exception {
      long targetId = stream.readLong();
      context.objectToBind(object, field, targetId);
    }
    
    @Override
    public void writeValue(DataOutput stream, Object object, Field field, ISerializeContext context) throws Exception {
      Object referencee = field.get(object);
      long objectId = context.serializeIfNotContained(referencee);
      stream.writeLong(objectId);
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

  public void readValue(DataInput input, Object object, Field field, IReaderContext context) {
    try {
      internalReadValue(input, object, field, context);
    } catch(Exception e) {
      throw new MemoriaException("could not read field: \n" + field + " field-type: " + name(), e);
    }
  }
  
  public abstract void writeValue(DataOutput input, Object object, Field field, ISerializeContext context) throws Exception;
  

  protected abstract void internalReadValue(DataInput input, Object object, Field field, IReaderContext context) throws Exception;

}
