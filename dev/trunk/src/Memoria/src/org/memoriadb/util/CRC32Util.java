package org.memoriadb.util;

import java.util.zip.CRC32;

public class CRC32Util {

  public static long getChecksum(byte[] data) {
    CRC32 crc32 = new CRC32();
    crc32.update(data);
    return crc32.getValue();
  }

}
