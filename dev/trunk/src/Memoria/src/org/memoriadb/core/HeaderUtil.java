package org.memoriadb.core;

import java.io.*;
import java.util.Arrays;

import org.memoriadb.exception.MemoriaException;

public final class HeaderUtil {
  
  public static final byte[] BLOCK_START_TAG = new byte[] {1, 2,3, 4};
  public static final byte[] BLOCK_END_TAG = new byte[] {4, 3, 2, 1};
  public static final byte[] TRANSACTION_START_TAG = new byte[] {5, 6, 7, 8};
  public static final byte[] TRANSACTION_END_TAG = new byte[] {8, 7, 6, 5};
  
  public static final int HEADER_SIZE = 4;
  
  //we resuse this buffer to read the header-tags
  private static final byte[] HEADER_BUFFER = new byte[HEADER_SIZE];
  
  static {
    if (BLOCK_START_TAG.length != HEADER_SIZE)       throw new MemoriaException("The block start tag was not specified correctly");
    if (BLOCK_END_TAG.length   != HEADER_SIZE)       throw new MemoriaException("The block end tag was not specified correctly");
    if (TRANSACTION_START_TAG.length != HEADER_SIZE) throw new MemoriaException("The transaction start tag was not specified correctly");
    if (TRANSACTION_END_TAG.length   != HEADER_SIZE) throw new MemoriaException("The transaction end tag was not specified correctly");
  }

  /**
   * 
   * @param data input data
   * @param start start index, where the tag should be in the data.
   * @param tagToAssert
   */
  public static void assertTag(byte[] data, int start, byte[] tagToAssert) {
    System.arraycopy(data, 0, HEADER_BUFFER, 0, 4);
    if (!Arrays.equals(HEADER_BUFFER, tagToAssert));    
  }

  public static void assertTag(DataInputStream stream, byte[] tagToAssert) throws IOException {
    stream.read(HEADER_BUFFER);
    if (!Arrays.equals(HEADER_BUFFER, tagToAssert)) throw new RuntimeException("Could not read block start");
  }

  private HeaderUtil() {}
  

}
