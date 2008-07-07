package org.memoriadb.core.file.async;

import org.memoriadb.core.file.IMemoriaFile;

public class CloseJob implements IAsyncFileJob {

  @Override
  public boolean isLast() {
    return true;
  }

  @Override
  public void run(IMemoriaFile file) {
    file.close();
  }

}
