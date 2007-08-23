package org.memoriadb.core.block;

import java.io.*;
import java.util.Arrays;

import org.memoriadb.exception.MemoriaException;

public final class BlockTagUtil {
  
  public static final byte[] BLOCK_START_TAG = new byte[] {1, 2,3, 4};
  
  public static final int TAG_SIZE = 4;
  
  //we resuse this buffer to read the header-tags
  private static final byte[] TAG_BUFFER = new byte[TAG_SIZE];
  
  static {
    if (BLOCK_START_TAG.length != TAG_SIZE)       throw new MemoriaException("The block start tag was not specified correctly");
  }

  public static void assertTag(DataInputStream stream, byte[] tagToAssert) throws IOException {
    stream.read(TAG_BUFFER);
    if (!Arrays.equals(TAG_BUFFER, tagToAssert)) throw new RuntimeException("Could not read block start");
  }

  private BlockTagUtil() {}
  

}
