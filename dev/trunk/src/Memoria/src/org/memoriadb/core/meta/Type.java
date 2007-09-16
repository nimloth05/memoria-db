package org.memoriadb.core.meta;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.ObjectFieldReference;
import org.memoriadb.exception.MemoriaException;


public enum Type {

  typeBoolean {

    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readBoolean();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeBoolean(((Boolean)value).booleanValue());
    }
    
  },
  typeChar {

    
    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readChar();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeChar(((Character)value).charValue());
    }

  },
  typeByte {

    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readByte();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeByte(((Byte)value).byteValue());
    }
    
  },
  typeShort {

    
    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readShort();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeShort(((Short)value).shortValue());
    }

  },
  typeInteger {

    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readInt();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeInt(((Integer)value).intValue());
    }

  },
  typeLong {

    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readLong();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeLong(((Long)value).longValue());
    }

  },
  typeFloat {

    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readFloat();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeFloat(((Float)value).floatValue());
    }

  },
  typeDouble {

    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readDouble();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeDouble(((Double)value).doubleValue());
    }

  },
  typeString {

    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readUTF();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeUTF(value.toString());
      } else {
        output.writeUTF("");
      }
    }

  },
  typeClass {
    
    @Override
    protected Object internalReadValue(DataInput input) throws IOException {
      return input.readLong();
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      long objectId = -1;
      if (value != null) {
        objectId = context.getObjectId(value);
      }
      output.writeLong(objectId);
    }

  };

  private static final Map<Class<?>, Type> sPrimitiveTypeMap = createTypeMap();

  public static Type getType(Class<?> javaType) {
    Type type = sPrimitiveTypeMap.get(javaType);
    if (type == null) return typeClass;
    return type;
  }
  
  public static Type getType(Field field) {
    return getType(field.getType());
  }
  
  public static Type getType(Object value) {
    return getType(value.getClass());
  }

  public static Type readType(DataInput input, IReaderContext context) {
    byte byteOrdinal = -1;
    try {
      byteOrdinal = input.readByte();
      return Type.values()[byteOrdinal];
    } catch (Exception e) {
      throw new MemoriaException("Could not read type Information. "  + byteOrdinal, e);
    }
  }

  public static void writeValueWithType(DataOutput output, Object value, ISerializeContext context) {
    Type type = getType(value);
    if (type.ordinal() > Byte.MAX_VALUE) throw new MemoriaException("Can not write back type. Type-Ordinal is bigger than Byte.MAX_VALUE");
      
    byte ordinalByte = (byte) type.ordinal();
      
    try {
      output.write(ordinalByte);
      type.internalWriteValue(output, value, context);
    }
    catch (Exception e) {
      throw new MemoriaException("could not write value. Type: "+ type.name() + " value: " + value, e);
    }
  }
  
  private static Map<Class<?>, Type> createTypeMap() {
    Map<Class<?>, Type> result = new HashMap<Class<?>, Type>();

    result.put(Boolean.class, typeBoolean);
    result.put(Boolean.TYPE, typeBoolean);

    result.put(Character.class, typeChar);
    result.put(Character.TYPE, typeChar);

    result.put(Byte.class, typeByte);
    result.put(Byte.TYPE, typeByte);

    result.put(Short.class, typeShort);
    result.put(Short.TYPE, typeShort);

    result.put(Integer.class, typeInteger);
    result.put(Integer.TYPE, typeInteger);

    result.put(Long.class, typeLong);
    result.put(Long.TYPE, typeLong);

    result.put(Float.class, typeFloat);
    result.put(Float.TYPE, typeFloat);

    result.put(Double.class, typeDouble);
    result.put(Double.TYPE, typeDouble);

    result.put(String.class, typeString);

    return result;
  }
  
  public void readFieldValue(DataInput input, Object object, Field field, IReaderContext context) {
    try {
      Object readValue = internalReadValue(input);
      if (this != Type.typeClass) {
        field.set(object, readValue);
      } else {
        context.objectToBind(new ObjectFieldReference(object, field, (Long)readValue)); 
      }
    } catch(Exception e) {
      throw new MemoriaException("could not read field: '" + field + "' field-type: " + name(), e);
    }
  }
  
  public Object readValue(DataInput input) {
    try {
      return internalReadValue(input);
    }
    catch (IOException e) {
      throw new MemoriaException("Could not read value " + name());
    }
  }
  
  public void writeFieldValue(DataOutput input, Object object, Field field, ISerializeContext context) {
    try {
      Object value = field.get(object);
      internalWriteValue(input, value, context);
    } catch (Exception e) {
      throw new MemoriaException("could not write value. Type: "+ name() + " field: " + field, e);
    }
  }
  
  protected abstract Object internalReadValue(DataInput input) throws IOException;
  
  protected abstract void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException;
  
}
