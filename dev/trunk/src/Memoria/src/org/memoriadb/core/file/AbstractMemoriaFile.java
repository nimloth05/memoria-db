package org.memoriadb.core.file;

import java.io.InputStream;

import org.memoriadb.core.exception.MemoriaException;

public abstract class AbstractMemoriaFile implements IMemoriaFile {

  private boolean fClosed = false;
  private boolean fLocked = false;

  @Override
  public final void append(byte[] data) {
    check();
    doAppend(data);
  }

  @Override
  public void close() {
    if (fClosed) return;
    fClosed = true;
    doClose();
  }

  @Override
  public final InputStream getInputStream() {
    return getInputStream(0);
  }
  
  @Override
  public final InputStream getInputStream(long position) {
    check();
    fLocked = true;
    return doGetInputStream(position);
  }

  @Override
  public final long getSize() {
    // no restrictions
    return doGetSize();
  }

  @Override
  public boolean isEmpty() {
    return getSize() == 0;
  }

  /**
   * Resets the state of this file, not touching it's data.
   */
  public void reset() {
    fClosed = false;
    fLocked = false;
  }

  @Override
  public final void write(byte[] data, long offset) {
    check();
    doWrite(data, offset);
  }

  protected abstract void doAppend(byte[] data);

  protected abstract void doClose();

  protected abstract InputStream doGetInputStream(long positio);

  protected abstract long doGetSize();

  protected abstract void doWrite(byte[] data, long offset);
  
  /**
   * Tells this implementation that the inputstream has been closed -> unlocks the interface.
   */
  protected void streamClosed() {
    fLocked = false;
  }
  
  /**
   * checks that the file is neither closed nor locked
   */
  private void check() {
    if (fClosed) throw new MemoriaException("file is closed");
    if (fLocked) throw new MemoriaException("file is locked. Close InputStreams");
  }

}
