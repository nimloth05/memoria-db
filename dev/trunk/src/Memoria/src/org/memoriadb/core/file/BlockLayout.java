package org.memoriadb.core.file;

import java.io.*;
import java.util.Arrays;

import org.memoriadb.exception.FileCorruptException;
import org.memoriadb.util.Constants;

/**
 * Sizes are always gross-values. The BlockSize for example includes it's data as well as its block-tag and size. 
 * @author msc
 */
public final class BlockLayout {
  
  public static final byte[] BLOCK_START_TAG = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
  
  public static final int BLOCK_TAG_LEN = BLOCK_START_TAG.length;
  public static final int BLOCK_SIZE_LEN = Constants.LONG_SIZE;
  public static final int TRX_SIZE_LEN = Constants.LONG_SIZE;
  public static final int REVISION_LEN = Constants.LONG_SIZE;
  public static final int CRC_LEN = Constants.LONG_SIZE;
  
  public static final int BLOCK_OVERHEAD = BLOCK_TAG_LEN + BLOCK_SIZE_LEN;
  
  public static final int TRX_OVERHEAD = TRX_SIZE_LEN +  REVISION_LEN + CRC_LEN;
  
  public static void assertBlockTag(DataInputStream stream) throws IOException {
    byte[] tagBuffer = new byte[BLOCK_TAG_LEN];
    stream.read(tagBuffer);
    if (!Arrays.equals(tagBuffer, BLOCK_START_TAG)) throw new FileCorruptException("Could not read block start-tag : " + Arrays.toString(tagBuffer));
  }
  
  /**
   * @param trxDataLength The length of the net-data in the transaction
   * 
   * @return The the required size in bytes a block with trxDataLength bytes data in the transaction.
   */
  public static int getBlockSize(int trxDataLength) {
    return BLOCK_OVERHEAD + TRX_OVERHEAD + trxDataLength;
  }

  private BlockLayout() {}
  

}
