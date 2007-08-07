package org.memoriadb.util;


public final class ByteUtil {
  
  public static final int INT_SIZE = 4;
  
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

  private ByteUtil() {}

}