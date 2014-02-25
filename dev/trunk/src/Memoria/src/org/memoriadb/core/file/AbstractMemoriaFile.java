/*
 * Copyright 2010 memoria db project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package org.memoriadb.core.file;

import org.memoriadb.core.exception.MemoriaException;

import java.io.InputStream;

/**
 * @author Sandro
 */
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
