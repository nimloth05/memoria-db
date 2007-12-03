package org.memoriadb.core.meta;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.util.Constants;
import org.memoriadb.id.IObjectId;

public enum Type {

  typeBoolean {

    @Override
    public Class<?> getClassLiteral() {
      return boolean.class;
    }

    @Override
    protected boolean canBeNull() {
      return false;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readBoolean());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeBoolean(((Boolean) value).booleanValue());
    }
    
  },

  typeBooleanC {

    @Override
    public Class<?> getClassLiteral() {
      return Boolean.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readBoolean());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeBoolean(((Boolean) value).booleanValue());
    }

  },

  typeChar {

    @Override
    public Class<?> getClassLiteral() {
      return char.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readChar());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeChar(((Character) value).charValue());
    }

  },

  typeCharC {

    @Override
    public Class<?> getClassLiteral() {
      return Character.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readChar());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeChar(((Character) value).charValue());
    }
  },

  typeByte {

    @Override
    public Class<?> getClassLiteral() {
      return byte.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readByte());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeByte(((Byte) value).byteValue());
    }

  },

  typeByteC {

    @Override
    public Class<?> getClassLiteral() {
      return Byte.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readByte());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeByte(((Byte) value).byteValue());
    }

  },

  typeShort {

    @Override
    public Class<?> getClassLiteral() {
      return short.class;
    }

    @Override
    protected boolean canBeNull() {
      return false;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readShort());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeShort(((Short) value).shortValue());
    }

  },

  typeShortC {

    @Override
    public Class<?> getClassLiteral() {
      return Short.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      
      visitor.visitPrimitive(this, input.readShort());

    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeShort(((Short) value).shortValue());
    }

  },

  typeInteger {

    @Override
    public Class<?> getClassLiteral() {
      return int.class;
    }

    @Override
    protected boolean canBeNull() {
      return false;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readInt());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeInt(((Integer) value).intValue());
    }

  },

  typeIntegerC {

    @Override
    public Class<?> getClassLiteral() {
      return Integer.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      
      visitor.visitPrimitive(this, input.readInt());

    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeInt(((Integer) value).intValue());
    }

  },

  typeLong {

    @Override
    public Class<?> getClassLiteral() {
      return long.class;
    }

    @Override
    protected boolean canBeNull() {
      return false;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readLong());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeLong(((Long) value).longValue());
    }

  },

  typeLongC {

    @Override
    public Class<?> getClassLiteral() {
      return Long.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readLong());

    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeLong(((Long) value).longValue());
    }

  },

  typeFloat {

    @Override
    public Class<?> getClassLiteral() {
      return float.class;
    }

    @Override
    protected boolean canBeNull() {
      return false;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readFloat());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeFloat(((Float) value).floatValue());
    }

  },

  typeFloatC {

    @Override
    public Class<?> getClassLiteral() {
      return Float.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readFloat());

    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeFloat(((Float) value).floatValue());
    }

  },

  typeDouble {

    @Override
    public Class<?> getClassLiteral() {
      return double.class;
    }

    @Override
    protected boolean canBeNull() {
      return false;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readDouble());
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeDouble(((Double) value).doubleValue());
    }

  },

  typeDoubleC {

    @Override
    public Class<?> getClassLiteral() {
      return Double.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readDouble());

    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeDouble(((Double) value).doubleValue());
    }

  },

  typeString {

    @Override
    public Class<?> getClassLiteral() {
      return String.class;
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, input.readUTF());

    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      output.writeUTF(value.toString());
    }

  },
  
  typeObjectId {
    
    @Override
    public Class<?> getClassLiteral() {
      throw new MemoriaException("Not supported");
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      visitor.visitPrimitive(this, context.readObjectId(input));
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      IObjectId id = (IObjectId) value;
      id.writeTo(output);
    }
    
  },
  
  typeClass {

    @Override
    public Class<?> getClassLiteral() {
      throw new MemoriaException("Not supported");
    }

    @Override
    protected boolean canBeNull() {
      return true;
    }

    @Override
    protected void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException {
      IObjectId ref = context.readObjectId(input);
      visitor.visitClass(this, ref);
    }
    
    @Override
    protected void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
      if (!context.contains(value)) throw new MemoriaException("trying to save reference to unsaved object (use saveAll): " + value);
      context.getExistingtId(value).writeTo(output);
    }

  };

  private static final Map<Class<?>, Type> sPrimitiveTypeMap = createTypeMap();

  public static Type getType(Class<?> javaType) {
    Type type = sPrimitiveTypeMap.get(javaType);
    if (type != null) return type;
    if (IObjectId.class.isAssignableFrom(javaType)) return typeObjectId;
    return typeClass;
  }

  public static Type getType(Field field) {
    return getType(field.getType());
  }

  public static Type getType(Object value) {
    return getType(value.getClass());
  }

  /**
   * @return true, if the given object is a primitive (int/Integer, etc). Enums are NOT primitives
   */
  public static boolean isPrimitive(Class<?> klass) {
    return getType(klass).isPrimitive();
  }

  /**
   * @return true, if the given object is a primitive (int/Integer, etc). Enums are NOT primitives
   */
  public static boolean isPrimitive(Object object) {
    return getType(object).isPrimitive();
  }

  public static <T extends ITypeVisitor> T readValueWithType(DataInput input, IReaderContext context, T visitor) {
    byte byteOrdinal = Constants.NULL_VALUE;
    try {
      byteOrdinal = input.readByte();
      if (byteOrdinal == Constants.NULL_VALUE) {
        visitor.visitNull();
        return visitor;
      }
      
      Type.values()[byteOrdinal].internalReadValue(input, visitor, context);
      return visitor;
    }
    catch (Exception e) {
      throw new MemoriaException("Could not read type Information. " + byteOrdinal, e);
    }
  }

  /**
   * 
   * @param output
   * @param value can be null
   * @param context
   * @throws IOException
   */
  public static void writeValueWithType(DataOutput output, Object value, ISerializeContext context) throws IOException {
    if (value == null) {
      output.writeByte(Constants.NULL_VALUE);
      return;
    }
    
    Type type = getType(value);
    if (type.ordinal() > Byte.MAX_VALUE) throw new MemoriaException("Can not write back type, type ordinal is bigger than Byte.MAX_VALUE");
    
    byte ordinalByte = (byte) type.ordinal();
    
    output.writeByte(ordinalByte);
    type.internalWriteValue(output, value, context);
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

  public abstract Class<?> getClassLiteral();
  
  public boolean isPrimitive() {
    return (this != Type.typeClass);
  }

  /**
   * performs null check
   * @param input
   * @param context
   * @param visitor
   * @throws IOException 
   */
  public void readValue(DataInput input, IReaderContext context, ITypeVisitor visitor) throws IOException {
    if (canBeNull()) {
      byte nullByte = input.readByte();
      if (nullByte == Constants.NULL_VALUE) {
        visitor.visitNull();
        return;
      }
    }

    internalReadValue(input, visitor, context);
  }

  /**
   * performs null check
   * 
   * @param output
   * @param value
   *          can be null
   * @param context
   * @throws IOException 
   */
  public void writeValue(DataOutput output, Object value, ISerializeContext context) throws IOException {
    if (canBeNull()) {
      if (value == null) {
        output.writeByte(Constants.NULL_VALUE);
        return;
      }
      output.writeByte(Constants.ASSIGNED_VALUE);
    }
    
    if(value == null) throw new MemoriaException("null not expected for " + this );
    internalWriteValue(output, value, context);
  }

  protected abstract boolean canBeNull();

  protected abstract void internalReadValue(DataInput input, ITypeVisitor visitor, IReaderContext context) throws IOException;
  
  protected abstract void internalWriteValue(DataOutput output, Object value, ISerializeContext context) throws IOException;

}
