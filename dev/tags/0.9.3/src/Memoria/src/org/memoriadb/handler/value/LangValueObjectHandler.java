/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.handler.value;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.io.*;

/**
 * @author Sandro
 */
public abstract class LangValueObjectHandler implements IHandler {

  public static class BooleanValueObjectHandler extends LangValueObjectHandler {

    @Override
    public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
      boolean value = input.readBoolean();
      return context.isInDataMode() ? new LangValueObject<Boolean>(value, typeId) : Boolean.valueOf(value);
    }
    
    @Override
    public String getClassName() {
      return Boolean.class.getName();
    }

    @Override
    public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
      Boolean boolValue = (obj instanceof LangValueObject<?>) ? (Boolean)((LangValueObject<?>)obj).get() : (Boolean)obj;
      output.writeBoolean(boolValue.booleanValue());
    }
  }
  
  public static class ByteValueObjectHandler extends LangValueObjectHandler {
    
    @Override
    public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
      byte value = input.readByte();
      return context.isInDataMode() ? new LangValueObject<Byte>(value, typeId) : Byte.valueOf(value);
    }
    
    @Override
    public String getClassName() {
      return Byte.class.getName();
    }
    
    @Override
    public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
      Byte value = (obj instanceof LangValueObject<?>) ? (Byte)((LangValueObject<?>)obj).get() : (Byte)obj;
      output.writeByte(value.byteValue());
    }
    
  }
  
  public static class CharacterValueObjectHandler extends LangValueObjectHandler {
    
    @Override
    public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
      char value = input.readChar();
      return context.isInDataMode() ? new LangValueObject<Character>(value, typeId) : Character.valueOf(value);
    }
    
    @Override
    public String getClassName() {
      return Character.class.getName();
    }

    @Override
    public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
      Character value = (obj instanceof LangValueObject<?>) ? (Character)((LangValueObject<?>)obj).get() : (Character)obj;
      output.writeChar(value.charValue());
    }
    
  }
  
  public static class DoubleValueObjectHandler extends LangValueObjectHandler {
    
    @Override
    public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
      double value = input.readDouble();
      return context.isInDataMode() ? new LangValueObject<Double>(value, typeId) : Double.valueOf(value);
    }
    
    @Override
    public String getClassName() {
      return Double.class.getName();
    }
    
    @Override
    public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
      Double value = (obj instanceof LangValueObject<?>) ? (Double)((LangValueObject<?>)obj).get() : (Double)obj;
      output.writeDouble(value.doubleValue());
    }
    
  }
  
  public static class FloatValueObjectHandler extends LangValueObjectHandler {
    
    @Override
    public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
      float value = input.readFloat();
      return context.isInDataMode() ? new LangValueObject<Float>(value, typeId) : Float.valueOf(value);
    }
    
    @Override
    public String getClassName() {
      return Float.class.getName();
    }
    
    @Override
    public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
      Float value = (obj instanceof LangValueObject<?>) ? (Float)((LangValueObject<?>)obj).get() : (Float)obj;
      output.writeFloat(value.floatValue());
    }
    
  }
  
  public static class IntegerValueObjectHandler extends LangValueObjectHandler {
    
    @Override
    public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
      int value = input.readInt();
      return context.isInDataMode() ? new LangValueObject<Integer>(value, typeId) : Integer.valueOf(value);
    }
    
    @Override
    public String getClassName() {
      return Integer.class.getName();
    }
    
    @Override
    public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
      Integer value = (obj instanceof LangValueObject<?>) ? (Integer)((LangValueObject<?>)obj).get() : (Integer)obj;
      output.writeInt(value.intValue());
    }
    
  }
  
  public static class LongValueObjectHandler extends LangValueObjectHandler {
    
    @Override
    public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
      long value = input.readLong();
      return context.isInDataMode() ? new LangValueObject<Long>(value, typeId) : Long.valueOf(value);
    }
    
    @Override
    public String getClassName() {
      return Long.class.getName();
    }
    
    @Override
    public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
      Long value = (obj instanceof LangValueObject<?>) ? (Long)((LangValueObject<?>)obj).get() : (Long)obj;
      output.writeLong(value.longValue());
    }
    
  }
  
  public static class ShortValueObjectHandler extends LangValueObjectHandler {
    
    @Override
    public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
      short value = input.readShort();
      return context.isInDataMode() ? new LangValueObject<Short>(value, typeId) : Short.valueOf(value);
    }
    
    @Override
    public String getClassName() {
      return Short.class.getName();
    }
    
    @Override
    public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
      Short value = (obj instanceof LangValueObject<?>) ? (Short)((LangValueObject<?>)obj).get() : (Short)obj;
      output.writeShort(value.shortValue());
    }
    
  }
  
  public static class StringValueObjectHandler extends LangValueObjectHandler {
    
    @Override
    public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
      String value = input.readUTF();
      return context.isInDataMode() ? new LangValueObject<String>(value, typeId) : String.valueOf(value);
    }
    
    @Override
    public String getClassName() {
      return String.class.getName();
    }
    
    @Override
    public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
      String value = (obj instanceof LangValueObject) ? (String)((LangValueObject<?>)obj).get() : (String)obj;
      output.writeUTF(value);
    }
    
  }

  @Override
  public final void checkCanInstantiateObject(String className, IInstantiator instantiator) {}

  @Override
  public abstract Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception;

  @Override
  public abstract void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception;
  
  @Override
  public final void traverseChildren(Object obj, IObjectTraversal traversal) {}
  

}
