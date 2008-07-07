/**
 * 
 */
package org.memoriadb.core.file.async;

import org.memoriadb.core.file.IMemoriaFile;

class AppendJob implements IAsyncFileJob {

  private final byte[] fData;

  public AppendJob(byte[] data) {
    fData = data;
  }

  @Override
  public boolean isLast() {
    return false;
  }

  @Override
  public void run(IMemoriaFile file) {
    file.append(fData);
  }
}