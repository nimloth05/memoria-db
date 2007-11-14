package org.memoriadb.util;



public final class ByteUtil {
  
  public static final int INT_SIZE = 4;

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

  /**
   * Write the given value in the given byte array at position 0
   */
  public static void writeInt(byte[] data, int value) {
    data[0] = (byte)(value >>> 24);
    data[1] = (byte)(value >>> 16);
    data[2] = (byte)(value >>>  8);
    data[3] = (byte)(value >>>  0);
  }
  
  private ByteUtil() {}

}
