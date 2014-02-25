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

package org.memoriadb.core.file.async;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.IMemoriaFileDecorator;

import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Sandro
 */
public class AsynchronousFile implements IMemoriaFile, IMemoriaFileDecorator {
  
  private final IMemoriaFile fFile;
  private long fSize;
  private final BlockingQueue<IAsyncFileJob> fQueue = new LinkedBlockingQueue<IAsyncFileJob>();
  private final Thread fThread;

  public AsynchronousFile(IMemoriaFile file) {
    if (file == null) throw new IllegalArgumentException("file is null");
    fFile = file;
    fSize = fFile.getSize();
    fThread = createAndStartWorker();
  }

  @Override
  public void append(byte[] data) {
    fSize += data.length;
    fQueue.add(new AppendJob(data));
  }

  @Override
  public void close() {
    fQueue.add(new CloseJob());
    joinWorkerThread();
  }

  @Override
  public IMemoriaFile getFile() {
    return fFile;
  }

  @Override
  public InputStream getInputStream() {
    return fFile.getInputStream();
  }

  @Override
  public InputStream getInputStream(long position) {
    return fFile.getInputStream(position);
  }

  @Override
  public long getSize() {
    return fSize;
  }

  @Override
  public boolean isEmpty() {
    return getSize() == 0;
  }

  @Override
  public void sync() {
    fQueue.add(new SyncFSJob());
  }

  @Override
  public void write(byte[] data, long offset) {
    fQueue.add(new WriteJob(data, offset));
  }

  private Thread createAndStartWorker() {
    Thread thread = new Thread(createWorkerRunnable());
    thread.start();
    return thread;
  }

  private Runnable createWorkerRunnable() {
    return new Runnable() {

      @Override
      public void run() {
        while(true) {
          try {
            IAsyncFileJob job = fQueue.take();
            job.run(fFile);
            if (job.isLast()) break;
          }
          catch (InterruptedException e) {
            throw new MemoriaException(e);
          }
        }
      }
    };
  }

  private void joinWorkerThread() {
    try {
      fThread.join();
    }
    catch (InterruptedException e) {
      throw new MemoriaException(e);
    }
  }

}
