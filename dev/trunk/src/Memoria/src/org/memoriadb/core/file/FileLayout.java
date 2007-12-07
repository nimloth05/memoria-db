package org.memoriadb.core.file;

import java.io.*;
import java.util.Arrays;

import org.memoriadb.core.mode.IObjectStoreExt;
import org.memoriadb.core.util.Constants;

/**
 * Sizes are always gross-values. The BlockSize for example includes it's data as well as its block-tag and size. 
 * @author msc
 */
public final class FileLayout {
  
  public static final int WRITE_MODE_APPEND = -1; // a new block is appended.
  public static final int WRITE_MODE_UPDATE = 1; // an existing block is updated
  
  public static final byte[] MEMORIA_TAG = new byte[] { 'm', 'e', 'm', 'o', 'r', 'i', 'a' };

  public static final byte[] BLOCK_START_TAG = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
  
  public static final int BLOCK_TAG_LEN = BLOCK_START_TAG.length;
  public static final int BLOCK_SIZE_LEN = Constants.LONG_LEN;
  public static final int TRX_SIZE_LEN = Constants.LONG_LEN;
  public static final int REVISION_LEN = Constants.LONG_LEN;
  public static final int CRC_LEN = Constants.LONG_LEN;
  
  public static final int BLOCK_OVERHEAD = BLOCK_TAG_LEN + BLOCK_SIZE_LEN + CRC_LEN;
  
  public static final int TRX_OVERHEAD = TRX_SIZE_LEN +  REVISION_LEN + CRC_LEN;

  public static final int OBJECT_SIZE_LEN = Constants.INT_LEN;
  
  public static final int OPF = Constants.INT_LEN;

  public static final int CURRENT_BLOCK_INFO_LEN = Constants.INT_LEN + 2 * Constants.LONG_LEN;
  public static final int CURRENT_BLOCK_INFO_START_POSITION = MEMORIA_TAG.length;

  /**
   * @param trxDataLength The length of the net-data in the transaction
   * 
   * @return The required net-size in bytes a block must have.
   */
  public static int getBlockSize(int trxDataLength) {
    return TRX_OVERHEAD + trxDataLength;
  }
  
  public static int getHeaderSize(int headerInfoSize) {
    return headerInfoSize + MEMORIA_TAG.length + CURRENT_BLOCK_INFO_LEN + Constants.INT_LEN + CRC_LEN;
  }
  
  /**
   * @return The overhead per object, depends on the size of the IObjectId
   */
  public static int getOPO(IObjectStoreExt objectStore) {
    return OBJECT_SIZE_LEN + 2*objectStore.getIdSize();
  }
  
  public static boolean testBlockTag(DataInputStream stream) throws IOException {
    byte[] tagBuffer = new byte[BLOCK_TAG_LEN];
    stream.readFully(tagBuffer);
    return Arrays.equals(tagBuffer, BLOCK_START_TAG);
  }

  private FileLayout() {}
  

}
