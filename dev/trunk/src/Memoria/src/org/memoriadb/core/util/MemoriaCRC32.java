package org.memoriadb.core.util;

import java.util.zip.CRC32;

public final class MemoriaCRC32 extends CRC32 {
  
  public void updateInt(long i){
    super.update(ByteUtil.asByteArray(i));
  }
  
  public void updateLong(long l){
    super.update(ByteUtil.asByteArray(l));
  }
  
}
