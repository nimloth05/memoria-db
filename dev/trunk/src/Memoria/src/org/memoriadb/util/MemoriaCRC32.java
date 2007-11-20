package org.memoriadb.util;

import java.util.zip.CRC32;

public final class MemoriaCRC32 extends CRC32 {
  
  public void updateLong(long l){
    super.update(ByteUtil.asByteArray(l));
  }
  
}
