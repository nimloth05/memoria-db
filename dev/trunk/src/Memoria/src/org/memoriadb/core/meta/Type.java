package org.memoriadb.core.meta;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.Constants;


public enum Type {

  typeBoolean {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readBoolean());
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeBoolean(((Boolean)value).booleanValue());
    }
    
  },
  
  typeBooleanC {
    
    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      byte nullByte = input.readByte();
      Object value = nullByte != Constants.NULL_PRIMITIVE_OBJECT ? input.readBoolean() : null;
      visitor.visitPrimitive(this, value);
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeByte(Constants.VALID_PRIMTIVE_OBJECT);
        output.writeBoolean(((Boolean)value).booleanValue());
        return;
      }
      
      output.writeByte(Constants.NULL_PRIMITIVE_OBJECT);
    }
    
  },
  
  typeChar {
    
    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readChar());
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeChar(((Character)value).charValue());
    }

  },
  
  typeCharC {
    
    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      byte nullByte = input.readByte();
      Object value = nullByte != Constants.NULL_PRIMITIVE_OBJECT ? input.readChar() : null;
      visitor.visitPrimitive(this, value);
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeByte(Constants.VALID_PRIMTIVE_OBJECT);
        output.writeChar(((Character)value).charValue());
        return;
      }
      
      output.writeByte(Constants.NULL_PRIMITIVE_OBJECT);
    }
  },
  
  typeByte {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readByte());
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeByte(((Byte)value).byteValue());
    }
    
  },
  
  typeByteC {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      byte nullByte = input.readByte();
      Object value = nullByte != Constants.NULL_PRIMITIVE_OBJECT ? input.readByte() : null;
      visitor.visitPrimitive(this, value);
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeByte(Constants.VALID_PRIMTIVE_OBJECT);
        output.writeByte(((Byte)value).byteValue());
        return;
      }
      
      output.writeByte(Constants.NULL_PRIMITIVE_OBJECT);
    }
    
  },
  
  typeShort {

    
    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readShort());
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeShort(((Short)value).shortValue());
    }

  },
  
  typeShortC {

    
    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      byte nullByte = input.readByte();
      Object value = nullByte != Constants.NULL_PRIMITIVE_OBJECT ? input.readShort() : null;
      visitor.visitPrimitive(this, value);
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeByte(Constants.VALID_PRIMTIVE_OBJECT);
        output.writeShort(((Short)value).shortValue());
        return;
      }
      
      output.writeByte(Constants.NULL_PRIMITIVE_OBJECT);
    }

  },

  
  typeInteger {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readInt());
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeInt(((Integer)value).intValue());
    }

  },
  
  typeIntegerC {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      byte nullByte = input.readByte();
      Object value = nullByte != Constants.NULL_PRIMITIVE_OBJECT ? input.readInt() : null;
      visitor.visitPrimitive(this, value);
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeByte(Constants.VALID_PRIMTIVE_OBJECT);
        output.writeInt(((Integer)value).intValue());
        return;
      }
      
      output.writeByte(Constants.NULL_PRIMITIVE_OBJECT);
    }

  },
  
  typeLong {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readLong());
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeLong(((Long)value).longValue());
    }

  },
  
  typeLongC {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      byte nullByte = input.readByte();
      Object value = nullByte != Constants.NULL_PRIMITIVE_OBJECT ? input.readLong() : null;
      visitor.visitPrimitive(this, value);
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeByte(Constants.VALID_PRIMTIVE_OBJECT);
        output.writeLong(((Long)value).longValue());
        return;
      }
      
      output.writeByte(Constants.NULL_PRIMITIVE_OBJECT);
    }

  },
  
  typeFloat {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readFloat());
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeFloat(((Float)value).floatValue());
    }

  },
  
  typeFloatC {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      byte nullByte = input.readByte();
      Object value = nullByte != Constants.NULL_PRIMITIVE_OBJECT ? input.readFloat() : null;
      visitor.visitPrimitive(this, value);
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeByte(Constants.VALID_PRIMTIVE_OBJECT);
        output.writeFloat(((Float)value).floatValue());
        return;
      }
      
      output.writeByte(Constants.NULL_PRIMITIVE_OBJECT);
    }

  },
  
  typeDouble {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readDouble());
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeDouble(((Double)value).doubleValue());
    }

  },
  
  typeDoubleC {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      byte nullByte = input.readByte();
      Object value = nullByte != Constants.NULL_PRIMITIVE_OBJECT ? input.readDouble() : null;
      visitor.visitPrimitive(this, value);
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeByte(Constants.VALID_PRIMTIVE_OBJECT);
        output.writeDouble(((Double)value).doubleValue());
        return;
      }
      
      output.writeByte(Constants.NULL_PRIMITIVE_OBJECT);
    }

  },

  
  typeString {

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      byte nullByte = input.readByte();
      Object value = nullByte != Constants.NULL_PRIMITIVE_OBJECT ? input.readUTF() : null;
      visitor.visitPrimitive(this, value);
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (value != null) {
        output.writeByte(Constants.VALID_PRIMTIVE_OBJECT);
        output.writeUTF(value.toString());
        return;
      }
      
      output.writeByte(Constants.NULL_PRIMITIVE_OBJECT);
    }

  },
  typeClass {
    
    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitClass(this, context.createFrom(input));
    }

    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      IObjectId objectId = context.getNullReference();
      if (value != null) {
        objectId = context.getObjectId(value);
      }
      objectId.writeTo(output);
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

  public static <T extends ITypeVisitor> T readValueWithType(DataInput input, IReaderContext context, T visitor) {
    byte byteOrdinal = -1;
    try {
      byteOrdinal = input.readByte();
      Type.values()[byteOrdinal].readValue(input, context, visitor);
      return visitor;
    } catch (Exception e) {
      throw new MemoriaException("Could not read type Information. "  + byteOrdinal, e);
    }
  }

  public static void writeValueWithType(DataOutput output, Object value, ISerializeContext context) {
    Type type = getType(value);
    if (type.ordinal() > Byte.MAX_VALUE) throw new MemoriaException("Can not write back type, type ordinal is bigger than Byte.MAX_VALUE");
      
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

    result.put(Boolean.class, typeBooleanC);
    result.put(Boolean.TYPE, typeBoolean);

    result.put(Character.class, typeCharC);
    result.put(Character.TYPE, typeChar);

    result.put(Byte.class, typeByteC);
    result.put(Byte.TYPE, typeByte);

    result.put(Short.class, typeShortC);
    result.put(Short.TYPE, typeShort);

    result.put(Integer.class, typeIntegerC);
    result.put(Integer.TYPE, typeInteger);

    result.put(Long.class, typeLongC);
    result.put(Long.TYPE, typeLong);

    result.put(Float.class, typeFloatC);
    result.put(Float.TYPE, typeFloat);

    result.put(Double.class, typeDoubleC);
    result.put(Double.TYPE, typeDouble);

    result.put(String.class, typeString);

    return result;
  }
  
  public void readValue(DataInput input, IReaderContext context, ITypeVisitor visitor) {
    try {
      internalReadValue(input, visitor, context);
    }
    catch (IOException e) {
      throw new MemoriaException("Could not read value " + name());
    }
  }
  
  public void writeValue(DataOutput output, Object value, ISerializeContext context) {
    try {
      internalWriteValue(output, value, context);
    } catch (Exception e) {
      throw new MemoriaException("could not write value: "+ name() + " value: " + value, e);
    }
  }
  
  protected abstract void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException;
  
  protected abstract void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException;
  
}
