package org.memoriadb.core.file;

import org.memoriadb.core.util.ZipUtil;

public final class ZipCompressor implements ICompressor {

  public ZipCompressor() {
  }
  
  @Override
  public byte[] compress(byte[] input) {
    return ZipUtil.compress(input);
  }

  @Override
  public byte[] decompress(byte[] input) {
    return ZipUtil.decompress(input);
  }

}
