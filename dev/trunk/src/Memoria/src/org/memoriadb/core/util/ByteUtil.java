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

package org.memoriadb.core.util;

import java.io.*;
import java.util.UUID;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.io.IDataInput;

public final class ByteUtil {
  
  public static final int INT_SIZE = 4;

  public static byte[] asByteArray(int value){
    byte[] result = new byte[4];
    result[0] = (byte)(value >>> 24);
    result[1] = (byte)(value >>> 16);
    result[2] = (byte)(value >>>  8);
    result[3] = (byte)(value >>>  0);
    return result;
  }

  public static byte[] asByteArray(long value){
    byte[] result = new byte[8];
    result[0] = (byte)(value >>> 56);
    result[1] = (byte)(value >>> 48);
    result[2] = (byte)(value >>> 40);
    result[3] = (byte)(value >>> 32);
    result[4] = (byte)(value >>> 24);
    result[5] = (byte)(value >>> 16);
    result[6] = (byte)(value >>>  8);
    result[7] = (byte)(value >>>  0);
    return result;
  }

  /**
   * 
   * @param data a binary buffer that contains an int at position "start".
   * @param start index of the first int-byte.
   * @return
   */
  public static int readInt(byte[] data, int start) {
    int int1 = data[start] & 0xff;
    int int2 = data[start+1] & 0xff;
    int int3 = data[start+2] & 0xff;
    int int4 = data[start+3] & 0xff;
    
    return ((int1 << 24) + (int2 << 16) + (int3 << 8) + (int4 << 0));
  }

  public static long readUnsignedLong(IDataInput in) throws IOException {
    long result=0;
    int shift = 0;
    byte b;
    do {
      b = in.readByte();
      result |= (long)(b&0x7F)<<shift;
      shift += 7;
    }
    while((b&0x80) != 0);
    
    return result;
  }
  
  public static UUID readUUID(IDataInput input) throws IOException {
    return new UUID(input.readLong(), input.readLong());
  }

  /**
   * Write the given value in the given byte array at position 0
   */
  public static void writeInt(byte[] data, int value) {
    data[0] = (byte)(value >>> 24);
    data[1] = (byte)(value >>> 16);
    data[2] = (byte)(value >>>  8);
    data[3] = (byte)(value >>>  0);
  }
  
  public static void writeUnsignedLong(long input, DataOutput out) throws IOException {
    if(input < 0) throw new MemoriaException("unsigned long expected but was: " + input);
      
    do {
      int value = ((int)input & 0x7F);
      input >>>= 7;
      if(input!=0) value |= 0x80;
      out.writeByte(value);
    }
    while(input != 0);
  }
  
  public static void writeUUID(DataOutput output, UUID uuid) throws IOException {
    output.writeLong(uuid.getMostSignificantBits());
    output.writeLong(uuid.getLeastSignificantBits());
  }

  private ByteUtil() {}

}
