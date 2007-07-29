package org.memoriadb.core;

import java.io.*;
import java.util.Arrays;

public final class HeaderUtil {
  
  public static final byte[] BLOCK_START_TAG = new byte[] {1, 2,3, 4};
  public static final byte[] BLOCK_END_TAG = new byte[] {4, 3, 2, 1};
  public static final byte[] TRANSACTION_START_TAG = new byte[] {5, 6, 7, 8};
  public static final byte[] TRANSACTION_END_TAG = new byte[] {8, 7, 6, 5};
  
  //we resuse this buffer to read the header-tags
  public static final byte[] HEADER_BUFFER = new byte[4];

  public static void assertTag(DataInputStream stream, byte[] tagToAssert) throws IOException {
    stream.read(HEADER_BUFFER);
    if (!Arrays.equals(HEADER_BUFFER, tagToAssert)) throw new RuntimeException("Could not read block start");
  }

  private HeaderUtil() {}
  

}
