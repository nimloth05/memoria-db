package org.memoriadb.core.block;

import java.io.*;
import java.util.Arrays;
import org.memoriadb.exception.FileCorruptException;

public final class BlockLayout {
  
  public static final byte[] BLOCK_START_TAG = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
  
  public static final int TAG_SIZE = BLOCK_START_TAG.length;
  
  public static void assertBlockTag(DataInputStream stream) throws IOException {
    
    byte[] tagBuffer = new byte[TAG_SIZE];
    
    stream.read(tagBuffer);
    if (!Arrays.equals(tagBuffer, BLOCK_START_TAG)) throw new FileCorruptException("Could not read block start-tag : " + Arrays.toString(tagBuffer));
  }

  private BlockLayout() {}
  

}
