package org.memoriadb.core.block;

import java.io.*;
import java.util.Arrays;
import org.memoriadb.exception.FileCorruptException;

public final class BlockLayout {
  
  public static final byte[] BLOCK_START_TAG = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
  
  public static final int TAG_SIZE = BLOCK_START_TAG.length;
  
  
  //we resuse this buffer to read the header-tags
  private static final byte[] TAG_BUFFER = new byte[TAG_SIZE];

  public static void assertBlockTag(DataInputStream stream) throws IOException {
    stream.read(TAG_BUFFER);
    if (!Arrays.equals(TAG_BUFFER, BLOCK_START_TAG)) throw new FileCorruptException("Could not read block start : " + Arrays.toString(TAG_BUFFER));
  }

  private BlockLayout() {}
  

}
