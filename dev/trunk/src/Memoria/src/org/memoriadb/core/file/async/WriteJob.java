package org.memoriadb.core.file.async;

import org.memoriadb.core.file.IMemoriaFile;

public class WriteJob implements IAsyncFileJob {

  private final byte[] fData;
  private final long fOffset;

  public WriteJob(byte[] data, long offset) {
    fData = data;
    fOffset = offset;
  }

  @Override
  public boolean isLast() {
    return false;
  }

  @Override
  public void run(IMemoriaFile file) {
    file.write(fData, fOffset);
  }

}
