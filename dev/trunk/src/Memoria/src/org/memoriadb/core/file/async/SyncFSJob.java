package org.memoriadb.core.file.async;

import org.memoriadb.core.file.IMemoriaFile;

public class SyncFSJob implements IAsyncFileJob {

  @Override
  public boolean isLast() {
    return false;
  }

  @Override
  public void run(IMemoriaFile file) {
    file.sync();
  }

}
